/*################################################################################################################
								**	loader main & validate methods **
################################################################################################################*/
-- ----------------------
-- routine loader_parent
-- ----------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_parent $$

CREATE PROCEDURE loader_parent(
/*	
**purpose	 : parent procedure to load data from loader tables to main tables. 
			   logic is written to select which of the two(loader_main & loader_main_partial) procedures to execute.
			   using the i/p sessionkey logic is written to check if the user has permissions to add data or not. 
**parameters : v_sessionkey cannot be null.
*/
	IN v_sessionkey CHAR(32))
PROCLABEL:BEGIN

	-- intilialize variables
	DECLARE v_procname, v_validateresult VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);

	SET v_spstartdate = NOW();

	SET v_procname = 'loader_parent';
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));


	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
		-- depending on the opeation set for the sessionkey select the procedure to execute.     
		IF(EXISTS(SELECT operation FROM loader_initialize WHERE _fk_sessionkey = v_sessionkey AND (operation = 'import' OR operation = 'copy'))) THEN
		BEGIN

			SET v_validateresult = (SELECT loader_validatepublicationversion(v_sessionkey));

			IF (v_validateresult = 'no-match') THEN
			BEGIN
				CALL loader_main(v_sessionkey);
				CALL loader_clear(v_sessionkey);
			END;
			ELSE
			BEGIN
				CALL _returnerror (v_procname, 'publication version already exists', NULL);
				LEAVE PROCLABEL;
			END;
			END IF;
		END;
		ELSEIF (EXISTS(SELECT operation FROM loader_initialize WHERE _fk_sessionkey = v_sessionkey AND (operation = 'partialimport' OR operation = 'edit'))) THEN
		BEGIN

			DROP TEMPORARY TABLE IF EXISTS temp_publication;
			CREATE TEMPORARY TABLE temp_publication(publicationkey VARCHAR(20));

			INSERT INTO temp_publication
			SELECT makepublicationkey(l.publishername, s._key, l.version) AS publicationkey
			FROM loader_publication l
				JOIN `subject` s ON l.`subject` = s.`name`
			WHERE l._fk_sessionkey = v_sessionkey;	
 
			IF (EXISTS(SELECT * FROM publication WHERE _key IN (SELECT publicationkey FROM temp_publication))) THEN
			BEGIN
				CALL loader_main_partial(v_sessionkey);
				CALL loader_clear(v_sessionkey);
			END;
			ELSE
			BEGIN
				CALL loader_main(v_sessionkey);
				CALL loader_clear(v_sessionkey);
			END;
			END IF;

		END;
		END IF;
	
	END;
	END IF;

	SET v_params = CONCAT('sessionkey=', v_sessionkey);
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

END$$

delimiter ;


-- ------------------------
-- routine loader_validate
-- ------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_validate $$

CREATE PROCEDURE loader_validate(
/*	
**purpose	 : to semantically validate the data in the loader tables
**parameters : v_sessionkey cannot be null.
*/
	IN v_sessionkey CHAR(32))
PROCLABEL:BEGIN

	DECLARE v_publisher, v_subject VARCHAR(50);
	DECLARE v_version, v_min, v_max, v_cnt, v_level, level_cnt, v_counter INT;
	DECLARE v_publicationdesc, v_grade, v_benchmark, v_key, v_procname VARCHAR(255);
	DECLARE v_sessionvalidate BIT;
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);

	SET v_spstartdate = NOW();
	SET v_procname = 'loader_validate';

	-- check if the user has valid permissions
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
    	-- clear table;
    	DELETE FROM loader_errors WHERE _fk_sessionkey = v_sessionkey;
    	
    	-- validate publication loader table
    	IF ((SELECT COUNT(*) FROM loader_publication WHERE _fk_sessionkey = v_sessionkey) <> 1) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'publication', 'publication sheet is empty or the number of rows is greater than 1', v_sessionkey);
    	END IF;
    
    
    	SET v_publisher = (SELECT publishername FROM loader_publication WHERE _fk_sessionkey = v_sessionkey);
    	SET v_subject = (SELECT `subject` FROM loader_publication WHERE _fk_sessionkey = v_sessionkey);
    	SET v_publicationdesc = (SELECT publicationdescription FROM loader_publication WHERE _fk_sessionkey = v_sessionkey);
    	SET v_version = (SELECT version FROM loader_publication WHERE _fk_sessionkey = v_sessionkey);

    		
    	IF(NOT EXISTS (SELECT * FROM publisher p WHERE p.`name` = v_publisher)) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'publication', 'publisher name is not exist in the database', v_sessionkey);
    	END IF;
    	
    	IF(NOT EXISTS (SELECT * FROM `subject` s WHERE s.`name` = v_subject)) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'publication', 'subject name is not exist in the database', v_sessionkey);
    	END IF;
    	
    	IF(v_publicationdesc IS NULL OR LENGTH(v_publicationdesc) < 1) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'publication', 'publication description can not be blank', v_sessionkey);
    	END IF;
    	
    	-- validate categories loader table
    	IF((SELECT COUNT(*) FROM loader_categories WHERE _fk_sessionkey = v_sessionkey) < 1) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'categories', 'categories sheet can not be empty', v_sessionkey);
    	END IF;
    
    
    	-- tree level must be contiguous number 1 --> n
    	SET v_min = (SELECT MIN(treelevel) FROM loader_categories WHERE _fk_sessionkey = v_sessionkey);
    	SET v_max = (SELECT MAX(treelevel) FROM loader_categories WHERE _fk_sessionkey = v_sessionkey);
    	SET v_cnt = (SELECT COUNT(*) FROM loader_categories WHERE _fk_sessionkey = v_sessionkey);
    	
    
    	IF(v_min <> 1 OR v_max <> v_cnt OR EXISTS (SELECT treelevel FROM loader_categories WHERE _fk_sessionkey = v_sessionkey GROUP BY treelevel HAVING COUNT(*) > 1)) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'categories', 'tree level must be contiguous number 1 --> n', v_sessionkey);
    	END IF;
    
    	IF(EXISTS (SELECT * FROM loader_categories WHERE _fk_sessionkey = v_sessionkey AND (category IS NULL OR LENGTH(category) < 1))) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'categories', 'category can not be blank', v_sessionkey);
    	END IF;
    	
    	-- socks sheet
    	IF(EXISTS (SELECT * FROM loader_socks WHERE _fk_sessionkey = v_sessionkey AND (knowledgecategory IS NULL OR LENGTH(knowledgecategory) < 1 OR description IS NULL OR LENGTH(description) < 1))) THEN
    		INSERT INTO loader_errors VALUES('fatal', 'socks', 'knowledgecategory and description can not be blank', v_sessionkey);
    	END IF;
    

    	INSERT INTO loader_errors
    	SELECT 'fatal', 'socks', CONCAT('socks knowledgecategory cannot be duplicate: ', knowledgecategory), v_sessionkey
    	FROM loader_socks
    	WHERE _fk_sessionkey = v_sessionkey
    	GROUP BY knowledgecategory
    	HAVING COUNT(knowledgecategory) > 1;
    	
    	-- standards sheet
    	-- levels must be in the treelevel of the categories sheet; level can not be blank
    	INSERT INTO loader_errors
    	SELECT DISTINCT 'fatal', 'standards', CONCAT('standard level must exist in the categories sheet: ', `level`), v_sessionkey
    	FROM loader_standards
    	WHERE _fk_sessionkey = v_sessionkey
    		AND `level` NOT IN (SELECT treelevel 
    							FROM loader_categories 
    							WHERE _fk_sessionkey = v_sessionkey);
    	
    	INSERT INTO loader_errors	
    	SELECT 'fatal', 'standards', CONCAT('standard [key] cannot be blank, the standard name is :', `name`), v_sessionkey
    	FROM loader_standards
    	WHERE _fk_sessionkey = v_sessionkey
    		AND (`key` IS NULL OR LENGTH(`key`) < 1);
    	
    	INSERT INTO loader_errors
    	SELECT 'fatal', 'standards', CONCAT('standard name cannot be blank, the standard [key] is :', `key`), v_sessionkey
    	FROM loader_standards
    	WHERE _fk_sessionkey = v_sessionkey
    		AND (`name` IS NULL OR LENGTH(`name`) < 1);
    
    	INSERT INTO loader_errors
    	SELECT 'fatal', 'standards', CONCAT('standard description cannot be blank, the standard [key] is :', `key`), v_sessionkey
    	FROM loader_standards
    	WHERE _fk_sessionkey = v_sessionkey
    		AND (description IS NULL OR LENGTH(description) < 1);
    
    	INSERT INTO loader_errors
    	SELECT 'fatal', 'standards', CONCAT('standard [key] cannot be duplicate: ', `key`), v_sessionkey
    	FROM loader_standards
    	WHERE _fk_sessionkey = v_sessionkey
    	GROUP BY `key`
    	HAVING COUNT(`key`) > 1;
    
    
    	DROP TEMPORARY TABLE IF EXISTS tempstandards;
    	CREATE TEMPORARY TABLE tempstandards(id INT AUTO_INCREMENT, `level` INT(11), `key` VARCHAR(150), PRIMARY KEY (id));
    
    	INSERT INTO tempstandards(`level`, `key`)
    	SELECT `level`, `key` 
    	FROM loader_standards
    	WHERE _fk_sessionkey = v_sessionkey;
    
    	SET v_counter = 1;
    
     	WHILE(v_counter <= (SELECT COUNT(*) FROM tempstandards)) DO
     	BEGIN
    
    		SET v_level = (SELECT `level` FROM tempstandards WHERE id = v_counter);
    		SET v_key = (SELECT `key` FROM tempstandards WHERE id = v_counter);
    		
    
    		DROP TEMPORARY TABLE IF EXISTS temp_funcbuildtable1;
    		CREATE TEMPORARY TABLE temp_funcbuildtable1 (idx INT, record VARCHAR(255));
    
    		CALL _buildtable1(v_key, '|');
    
    		SET level_cnt = (SELECT COUNT(*) FROM temp_funcbuildtable1);
    
    		IF (level_cnt <> v_level) THEN 
    			INSERT INTO loader_errors
    			SELECT 'fatal', 'standards', CONCAT('standard key ', v_key, ' is invalid for level ', v_level), v_sessionkey;
    		END IF;
    
    		INSERT INTO loader_errors
    		SELECT 'fatal', 'standards', CONCAT('parent standard key level ', record, ' is missing'), v_sessionkey
    		FROM temp_funcbuildtable1
    		WHERE LTRIM(RTRIM(record)) NOT IN (SELECT `key` FROM loader_standards WHERE _fk_sessionkey = v_sessionkey);
    
    		SET v_counter = v_counter + 1;
    
		END;
		END WHILE;
    
    
    	-- benchmark grades sheet
    	INSERT INTO loader_errors
    	SELECT DISTINCT 'fatal', 'benchmark grades', CONCAT('benchmark key and/or parent are not exists in the standards sheet: ', benchmark), v_sessionkey
    	FROM loader_benchmarkgrades 
    	WHERE _fk_sessionkey = v_sessionkey
    		AND benchmark NOT IN (SELECT `key` FROM loader_standards WHERE _fk_sessionkey = v_sessionkey);
    	
    	-- support comma delimiter grades
    	DROP TABLE IF EXISTS tempbenchmarkgrades;
    	CREATE TEMPORARY TABLE tempbenchmarkgrades(id INT NOT NULL AUTO_INCREMENT, benchmark VARCHAR(200), grade VARCHAR(255), PRIMARY KEY (id));
    
    	INSERT INTO tempbenchmarkgrades(benchmark, grade)
    	SELECT benchmark, grade 
    	FROM loader_benchmarkgrades
    	WHERE _fk_sessionkey = v_sessionkey;
    
		SET v_counter = 1;

    	WHILE(v_counter <= (SELECT COUNT(*) FROM tempbenchmarkgrades)) DO
    	BEGIN
    		SET v_benchmark = (SELECT benchmark FROM tempbenchmarkgrades WHERE id = v_counter);
    		SET v_grade = (SELECT grade FROM tempbenchmarkgrades WHERE id = v_counter); 
    
    		DROP TEMPORARY TABLE IF EXISTS temp_funcbuildtable;
    		CREATE TEMPORARY TABLE temp_funcbuildtable (idx INT, record VARCHAR(255));
    
    		CALL _buildtable(v_grade, ',');
    
    		-- comma delimiter grades
    		INSERT INTO loader_errors
    		SELECT DISTINCT 'fatal', 'benchmark grades', CONCAT('grade is not exists in the database, we may need to add: ', RTRIM(LTRIM(record))), v_sessionkey
    		FROM temp_funcbuildtable
    		WHERE RTRIM(LTRIM(record)) NOT IN (SELECT _key FROM gradelevel);
    	
			SET v_counter = v_counter + 1;
    	END;
    	END WHILE;
    
    	-- standard relationship sheet
    	INSERT INTO loader_errors
    	SELECT DISTINCT 'fatal', 'standardrelationship', CONCAT('standard a key must exists in the standards sheet: [', standard_a, ']'), v_sessionkey
    	FROM loader_standard_relationship 
    	WHERE _fk_sessionkey = v_sessionkey
    		AND standard_a NOT IN (SELECT `key` FROM loader_standards WHERE _fk_sessionkey = v_sessionkey);
    
    	INSERT INTO loader_errors
    	SELECT DISTINCT 'fatal', 'standardrelationship', CONCAT('standard b key must exists in the standards sheet: [', standard_b, ']'), v_sessionkey
    	FROM loader_standard_relationship 
    	WHERE _fk_sessionkey = v_sessionkey
    		AND standard_b NOT IN (SELECT `key` FROM loader_standards WHERE _fk_sessionkey = v_sessionkey);
    	
    	INSERT INTO loader_errors
    	SELECT DISTINCT 'fatal', 'standardrelationship', CONCAT('relationship type must exists in a predefined set: [', relationshiptype, ']'), v_sessionkey
    	FROM loader_standard_relationship 
    	WHERE _fk_sessionkey = v_sessionkey
    		AND relationshiptype NOT IN (SELECT `name` FROM relationshiptype WHERE _fk_sessionkey = v_sessionkey);
    
    	INSERT INTO loader_errors
    	SELECT DISTINCT 'fatal', 'standardrelationship', CONCAT('standard relationship can not be duplicate: [', standard_a, '][', standard_b, '][', relationshiptype, ']'), v_sessionkey
    	FROM loader_standard_relationship 
    	WHERE _fk_sessionkey = v_sessionkey
    	GROUP BY standard_a, standard_b, relationshiptype
    	HAVING COUNT(*) > 1;
    
    
    	IF (EXISTS (SELECT * FROM loader_errors WHERE _fk_sessionkey = v_sessionkey)) THEN
            SELECT * 
    		FROM loader_errors 
    		WHERE _fk_sessionkey = v_sessionkey;
    	END IF;
    
	END;
	END IF;

	SET v_params = CONCAT('sessionkey=', v_sessionkey);
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

END$$

delimiter ;


-- --------------------
-- routine loader_main
-- --------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_main $$

CREATE PROCEDURE loader_main(
/*	
**purpose	 : to load entire data set from loader tables based on input session key
**parameters : v_sessionkey cannot be null.
*/
	IN v_sessionkey CHAR(32))
PROCLABEL: BEGIN  
  
	DECLARE v_publisherkey VARCHAR(8);
	DECLARE v_version, v_counter INT;
	DECLARE v_subjectkey VARCHAR(10);
	DECLARE v_publicationkey VARCHAR(20);
	DECLARE v_benchmark VARCHAR(200);
	DECLARE v_grade, v_procname VARCHAR(255);
	DECLARE v_sessionvalidate BIT;
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);

	SET v_spstartdate = NOW();
	SET v_procname = 'loader_main';

	-- validate if the user has permissions
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  

	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
		-- call validation sp loader_validate, the procedure will load table loader_errors with errors if any
		CALL loader_validate(v_sessionkey);
	
		IF ((SELECT COUNT(*) FROM loader_errors WHERE _fk_sessionkey = v_sessionkey) > 1) THEN
		BEGIN
			CALL _returnerror ('loader_main', 'failed validation', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;  

		-- get publisherkey, subjectkey and version information to generate publicationkey
		SET v_publisherkey = (SELECT DISTINCT p._key 
							  FROM loader_publication l 
								   JOIN publisher p ON l.publishername = p.`name` 
							  WHERE l._fk_sessionkey = v_sessionkey);

		SET v_subjectkey = (SELECT DISTINCT s._key 
							FROM loader_publication l 
								JOIN `subject` s ON l.`subject` = s.`name` 
							WHERE l._fk_sessionkey = v_sessionkey);

		SET v_version = (SELECT DISTINCT version 
						 FROM loader_publication 
						 WHERE _fk_sessionkey = v_sessionkey);
		
		-- generate publicationkey
		SET v_publicationkey = (SELECT makepublicationkey(v_publisherkey, v_subjectkey, v_version));

		-- load publication data
		INSERT INTO publication (_key, _fk_publisher, version, _fk_subject, subjectlabel, description)
		SELECT v_publicationkey 
			 , v_publisherkey
			 , v_version
			 , v_subjectkey
			 , `subject` AS subjectlabel
			 , RTRIM(LTRIM(publicationdescription)) AS publicationdescription
		FROM loader_publication
		WHERE _fk_sessionkey = v_sessionkey;
	
		-- load standard_category
		INSERT INTO standard_category(`name`, treelevel, _fk_publication)
		SELECT category, treelevel, v_publicationkey 
		FROM loader_categories
		WHERE _fk_sessionkey = v_sessionkey;
				
		-- load standards
		INSERT INTO standard(_key, `name`, _fk_parent, _fk_publication, description, treelevel, pubkey, shortname)
		SELECT makestandardkey(v_publicationkey, ls.`key`)
			 , ls.`name`
			 , CASE WHEN(sc.treelevel = 1) THEN NULL
					ELSE makestandardkey(v_publicationkey, LEFT(ls.`key`, _lastindex (ls.`key`, '|'))) 
			   END AS _fk_parent
			 , v_publicationkey AS _fk_publication
			 , ls.description
			 , ls.`level`
			 , ls.`key`
			 , ls.shortname
		FROM loader_standards ls
			JOIN standard_category sc ON ls.`level` = sc.treelevel
		WHERE sc._fk_publication = v_publicationkey
			AND ls._fk_sessionkey = v_sessionkey;
		
	
		-- load standard grades
		DROP TEMPORARY TABLE IF EXISTS temp_benchmarkgrades;
		CREATE TEMPORARY TABLE temp_benchmarkgrades (id INT NOT NULL AUTO_INCREMENT,
													 benchmark VARCHAR(200),
													 grade VARCHAR(255),
													 PRIMARY KEY(id));


		INSERT INTO temp_benchmarkgrades(benchmark, grade)
		SELECT benchmark, grade
		FROM loader_benchmarkgrades
		WHERE _fk_sessionkey = v_sessionkey;

		SET v_counter = 1;
	
		WHILE(v_counter <= (SELECT COUNT(*) FROM temp_benchmarkgrades)) DO
		BEGIN
			SET v_benchmark = (SELECT benchmark FROM temp_benchmarkgrades WHERE id = v_counter);
			SET v_grade = (SELECT grade FROM temp_benchmarkgrades WHERE id = v_counter); 
		
		
			DROP TEMPORARY TABLE IF EXISTS temp_funcbuildtable1;
			CREATE TEMPORARY TABLE temp_funcbuildtable1 (idx INT, record VARCHAR(255));

			DROP TEMPORARY TABLE IF EXISTS temp_funcbuildtable;
			CREATE TEMPORARY TABLE temp_funcbuildtable (idx INT, record VARCHAR(255));

			CALL _buildtable1(v_benchmark, '|');
			CALL _buildtable(v_grade, ',');

			INSERT INTO standard_grade(_fk_standard, _fk_gradelevel) -- comma delimiter grades
			SELECT s._key
				 , RTRIM(LTRIM(g.record)) 
			FROM temp_funcbuildtable1 t, standard s
				 CROSS JOIN temp_funcbuildtable g
			WHERE s._fk_publication = v_publicationkey 
				 AND s.pubkey = t.record	
				 AND NOT EXISTS (SELECT * 
								 FROM standard_grade 
								 WHERE _fk_standard = s._key 
									AND _fk_gradelevel=RTRIM(LTRIM(g.record)));

			SET v_counter = v_counter + 1;

		END;
		END WHILE;


		-- load socks
		INSERT INTO sock_drawer(_key, `name`, description, _fk_publication)
		SELECT makesockskey(v_publicationkey, knowledgecategory)
			 , knowledgecategory
			 , description
			 , v_publicationkey
		FROM  loader_socks
		WHERE _fk_sessionkey = v_sessionkey;

	
		-- load standard relationships
		INSERT INTO standard_relationship(_fk_standard_a, _fk_standard_b, _fk_relationshiptype)
		SELECT makestandardkey(v_publicationkey, standard_a)
			 , makestandardkey(v_publicationkey, standard_b)
			 , _key 
		FROM loader_standard_relationship lr
			JOIN relationshiptype r ON lr.relationshiptype = r.`name`
		WHERE lr._fk_sessionkey = v_sessionkey;

	END;
	END IF;

	SET v_params = CONCAT('sessionkey=', v_sessionkey);
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

END$$

delimiter ;


-- ----------------------------
-- routine loader_main_partial
-- ----------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_main_partial $$

CREATE PROCEDURE loader_main_partial(
/*	
**purpose	 : to load partial data set from loader tables based on input session key
**parameters : v_sessionkey cannot be null.
*/
	v_sessionkey CHAR(32))
PROCLABEL: BEGIN  

	DECLARE v_publisherkey VARCHAR(8);
	DECLARE v_version, v_counter INT;
	DECLARE v_subjectkey VARCHAR(10);
	DECLARE v_publicationkey VARCHAR(20);
	DECLARE v_benchmark VARCHAR(200);
	DECLARE v_grade, v_procname VARCHAR(255);
	DECLARE v_sessionvalidate BIT;
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);

	SET v_spstartdate = NOW();
	SET v_procname = 'loader_main_partial';

	-- check if the user has valid permissions
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
		-- call validation sp loader_validate, the procedure will load table loader_errors with errors if any
		CALL loader_validate(v_sessionkey);
	
		IF ((SELECT COUNT(*) FROM loader_errors WHERE _fk_sessionkey = v_sessionkey) > 0) THEN
		BEGIN
			CALL _returnerror ('loader_main_partial', 'failed loader validation', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;   

		-- get publisherkey, subjectkey and version information to generate publicationkey
		SET v_publisherkey = (SELECT DISTINCT p._key 
							  FROM loader_publication l 
								   JOIN publisher p ON l.publishername = p.`name` 
							  WHERE l._fk_sessionkey = v_sessionkey);

		SET v_subjectkey = (SELECT DISTINCT s._key 
							FROM loader_publication l 
								JOIN `subject` s ON l.`subject` = s.`name` 
							WHERE l._fk_sessionkey = v_sessionkey);

		SET v_version = (SELECT DISTINCT version 
						 FROM loader_publication 
						 WHERE _fk_sessionkey = v_sessionkey);
		
		-- generate publicationkey
		SET v_publicationkey = (SELECT makepublicationkey(v_publisherkey, v_subjectkey, v_version));


		-- update publication description
		UPDATE publication p, loader_publication l, `subject` s
		SET p.description = RTRIM(LTRIM(l.publicationdescription))
		WHERE l.`subject` = s.`name`
			AND p._key = v_publicationkey
			AND l._fk_sessionkey = v_sessionkey;
	
		-- update/add new standard_category names
		INSERT INTO standard_category(`name`, treelevel, _fk_publication)
		SELECT category, treelevel, v_publicationkey 
		FROM loader_categories lc
		WHERE _fk_sessionkey = v_sessionkey
			AND  NOT EXISTS (SELECT treelevel 
							 FROM standard_category sc
							 WHERE sc.treelevel = lc.treelevel
								AND _fk_publication = v_publicationkey);


		UPDATE standard_category sc, loader_categories lc 
		SET `name` = category
		WHERE sc.treelevel = lc.treelevel 
			AND sc._fk_publication = v_publicationkey
			AND lc._fk_sessionkey = v_sessionkey;

		-- update/add new standards
		INSERT INTO standard(_key, `name`, _fk_parent, _fk_publication, description, treelevel, pubkey, shortname)
		SELECT makestandardkey(v_publicationkey, ls.`key`)
			 , ls.`name`
			 , CASE WHEN(sc.treelevel = 1) THEN NULL
					ELSE makestandardkey(v_publicationkey, LEFT(ls.`key`, _lastindex (ls.`key`, '|'))) 
			   END AS _fk_parent
			 , v_publicationkey AS _fk_publication
			 , ls.description
			 , ls.`level`
			 , ls.`key`
			 , ls.shortname
		FROM loader_standards ls
			JOIN standard_category sc ON ls.`level` = sc.treelevel	
		WHERE sc._fk_publication = v_publicationkey 
			AND ls._fk_sessionkey = v_sessionkey
			AND NOT EXISTS (SELECT _key 
							FROM standard s
							WHERE s._key = makestandardkey(v_publicationkey, ls.`key`) 
								AND _fk_publication = v_publicationkey);
		

		UPDATE standard s, loader_standards ls
		SET s.`name` = ls.`name`
		  , s.description = ls.description
		  , s.shortname = ls.shortname	
		WHERE s._fk_publication = v_publicationkey 
			AND ls.`level` = s.treelevel	
			AND s._key = makestandardkey(v_publicationkey, ls.`key`)
			AND ls._fk_sessionkey = v_sessionkey;


		-- add new standard grades
		DROP TEMPORARY TABLE IF EXISTS temp_benchmarkgrades;
		CREATE TEMPORARY TABLE temp_benchmarkgrades(id INT NOT NULL AUTO_INCREMENT,
												   benchmark VARCHAR(200),
												   grade VARCHAR(255),
												   PRIMARY KEY(id));

		INSERT INTO temp_benchmarkgrades(benchmark, grade)
		SELECT benchmark, grade 
		FROM loader_benchmarkgrades
		WHERE _fk_sessionkey = v_sessionkey;

		SET v_counter = 1;

		WHILE(v_counter <= (SELECT COUNT(*) FROM temp_benchmarkgrades)) DO
		BEGIN
			SET v_benchmark = (SELECT benchmark FROM temp_benchmarkgrades WHERE id = v_counter);
			SET v_grade = (SELECT grade FROM temp_benchmarkgrades WHERE id = v_counter); 

			DROP TEMPORARY TABLE IF EXISTS temp_funcbuildtable1;
			CREATE TEMPORARY TABLE temp_funcbuildtable1 (idx INT, record VARCHAR(255));

			DROP TEMPORARY TABLE IF EXISTS temp_funcbuildtable;
			CREATE TEMPORARY TABLE temp_funcbuildtable (idx INT, record VARCHAR(255));

			CALL _buildtable1(v_benchmark, '|');
			CALL _buildtable(v_grade, ',');
		
			INSERT INTO standard_grade(_fk_standard, _fk_gradelevel) 
			SELECT s._key
				 , RTRIM(LTRIM(g.record)) 
			FROM temp_funcbuildtable1 t
				 JOIN standard s ON s.pubkey = t.record	
				 CROSS JOIN temp_funcbuildtable g
			WHERE s._fk_publication = v_publicationkey 
				 AND NOT EXISTS (SELECT * 
								 FROM standard_grade 
								 WHERE _fk_standard = s._key 
									AND _fk_gradelevel = RTRIM(LTRIM(g.record)));

			SET v_counter = v_counter + 1;

		END;
		END WHILE;


		-- update/add new socks
		INSERT INTO sock_drawer(_key, `name`, description, _fk_publication)
		SELECT makesockskey(v_publicationkey, knowledgecategory), knowledgecategory, description, v_publicationkey
		FROM loader_socks
		WHERE _fk_sessionkey = v_sessionkey
			AND makesockskey(v_publicationkey, knowledgecategory) NOT IN (SELECT _key 
																		  FROM sock_drawer 
																		  WHERE _fk_publication = v_publicationkey);

		UPDATE sock_drawer sd, loader_socks ls 
		SET sd.description = ls.description		
		WHERE ls._fk_sessionkey = v_sessionkey
			AND sd._key = makesockskey(v_publicationkey, knowledgecategory) 
			AND sd._fk_publication = v_publicationkey;
		
		-- add new standard relationships
		-- note: this is a hack. we will always reload all "prerequisite" relationship type for this publication.
		DELETE FROM standard_relationship 
		WHERE _fk_relationshiptype = 'prereq' 
			AND EXISTS (SELECT _key 
						FROM standard 
						WHERE _fk_publication = v_publicationkey AND (_fk_standard_a = _key OR _fk_standard_b = _key));


		INSERT INTO standard_relationship(_fk_standard_a, _fk_standard_b, _fk_relationshiptype)
		SELECT makestandardkey(v_publicationkey, standard_a), makestandardkey(v_publicationkey, standard_b), _key 
		FROM loader_standard_relationship lr, relationshiptype r
		WHERE lr.relationshiptype = r.`name`
			AND lr._fk_sessionkey = v_sessionkey
			AND NOT EXISTS (SELECT * 
							FROM standard_relationship sr 
							WHERE sr._fk_standard_a = makestandardkey(v_publicationkey, lr.standard_a)
			AND sr._fk_standard_b = makestandardkey(v_publicationkey, lr.standard_b));


	END;
	END IF;

	SET v_params = CONCAT('sessionkey=', v_sessionkey);
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

END$$

delimiter ;


/*################################################################################################################
								**	loader add methods **
################################################################################################################*/
-- ---------------------------------
-- routine loader_addbenchmarkgrades
-- ---------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS `loader_addbenchmarkgrades` $$

CREATE PROCEDURE `loader_addbenchmarkgrades`(
/*	
**purpose	 : to add data from loader_benchmarkgrades table.
			   using the i/p sessionkey logic is written to check if the user has permissions to add data or not. 
**parameters : v_sessionkey cannot be null.
			   v_benchmark cannot be null.
			   v_grade cannot be null.
*/
	IN v_sessionkey CHAR(32),
	IN v_benchmark VARCHAR(150), 
	IN v_grade VARCHAR(255))

PROCLABEL:BEGIN


	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_addbenchmarkgrade';

	-- check if the user have valid permissions
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
	
	    INSERT INTO loader_benchmarkgrades(benchmark, grade, _fk_sessionkey) VALUES
		(v_benchmark, v_grade, v_sessionkey); 	

	END;
	END IF;

END$$

delimiter ;


-- ----------------------------
-- routine loader_addcategories
-- ----------------------------
delimiter $$

DROP PROCEDURE IF EXISTS `loader_addcategories` $$

CREATE PROCEDURE `loader_addcategories`(
/*	
**purpose	 : to load data into loader_categories table.
			   using the i/p sessionkey logic is written to check if the user has permissions to add data or not. 
**parameters : v_sessionkey cannot be null.
			   v_category cannot be null.
			   v_treelevel cannot be null.
*/
	IN v_sessionkey CHAR(32),
	IN v_category VARCHAR(50),
	IN v_treelevel INT(11))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_addcategory';

	-- check if the user have valid permissions  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
	
	    INSERT INTO loader_categories(category, treelevel, _fk_sessionkey) VALUES
		(v_category, v_treelevel, v_sessionkey); 	

	END;
	END IF;

END$$

delimiter ;


-- ------------------------------
-- routine loader_addpublication
-- ------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS `loader_addpublication` $$

CREATE PROCEDURE `loader_addpublication`(
/*	
**purpose	 : to load data into loader_publication table.
			   using the i/p sessionkey logic is written to check if the user has permissions to add data or not. 
*/
	IN v_sessionkey CHAR(32),
	IN v_publishername VARCHAR(25), 
	IN v_publicationdesc VARCHAR(255),
	IN v_subject VARCHAR(50),
	IN v_version INT(11))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_addpublication';
  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
	
	    INSERT INTO loader_publication(publishername, publicationdescription, `subject`, version, _fk_sessionkey) VALUES
		(v_publishername, v_publicationdesc, v_subject, v_version, v_sessionkey); 	

	END;
	END IF;

END$$

delimiter ;


-- -----------------------
-- routine loader_addsocks
-- -----------------------
delimiter $$

DROP PROCEDURE IF EXISTS `loader_addsocks` $$

CREATE PROCEDURE `loader_addsocks`(
/*	
**purpose	 : to load data into loader_socks table.
			   using the i/p sessionkey logic is written to check if the user has permissions to add data or not. 
*/
	IN v_sessionkey CHAR(32),
	IN v_knowledgecategory VARCHAR(50), 
	IN v_description VARCHAR(500))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_addsocks';
  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
	
	    INSERT INTO loader_socks(knowledgecategory, description, _fk_sessionkey) VALUES
		(v_knowledgecategory, v_description, v_sessionkey); 	

	END;
	END IF;

END$$

delimiter ;


-- ---------------------------
-- routine loader_addstandards
-- ---------------------------
delimiter $$

DROP PROCEDURE IF EXISTS `loader_addstandards` $$

CREATE PROCEDURE `loader_addstandards`(
/*	
**purpose	 : to load data into loader_standards table.
			   using the i/p sessionkey logic is written to check if the user has permissions to add data or not. 
*/
	IN v_sessionkey CHAR(32),
	IN v_level INT(11), 
	IN v_key VARCHAR(150),
	IN v_name VARCHAR(150),
	IN v_description TEXT,
	IN v_shortname VARCHAR(100))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_addstandards';
  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
	
	    INSERT INTO loader_standards(`level`, `key`, `name`, description, shortname, _fk_sessionkey) VALUES
		(v_level, v_key, v_name, v_description, v_shortname, v_sessionkey); 	

	END;
	END IF;

END$$

delimiter ;


-- ---------------------------------------
-- routine loader_addstandardrelationship
-- ---------------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS `loader_addstandardrelationship` $$

CREATE PROCEDURE `loader_addstandardrelationship`(
/*	
**purpose	 : to load data into loader_standardrelationship table.
			   using the i/p sessionkey logic is written to check if the user has permissions to add data or not. 
*/
	IN v_sessionkey CHAR(32),
	IN v_standardakey VARCHAR(170), 
	IN v_standardbkey VARCHAR(170),
	IN v_relationshiptype VARCHAR(15))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_addstandardrelationship';
  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
	
	    INSERT INTO loader_standardrelationship(v_standardkeya, v_standardkeyb, relationshiptype, _fk_sessionkey) VALUES
		(v_standard_a, v_standard_b, v_relationshiptype, v_sessionkey); 	

	END;
	END IF;

END$$

delimiter ;


/*################################################################################################################
								**	loader get methods **
################################################################################################################*/
-- ----------------------------------
-- routine loader_getbenchmarkgrades
-- ----------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_getbenchmarkgrades $$

CREATE PROCEDURE loader_getbenchmarkgrades(
/*	
**purpose	 : to retreive data from loader_benchmarkgrades table for a given session key.
**parameters : v_sessionkey cannot be null.
*/
	IN v_sessionkey CHAR(32))
BEGIN  
	
	SELECT benchmark, grade
	FROM loader_benchmarkgrades
	WHERE _fk_sessionkey = v_sessionkey
  UNION
	-- to get all benchmarks that are not associated with any of the grades(benchmarks are standards of the highest level)
	SELECT  `key`, NULL 
	FROM loader_standards s
	WHERE _fk_sessionkey = v_sessionkey
		AND `level` = ( SELECT MAX(treelevel) 
					    FROM loader_categories 
						WHERE _fk_sessionkey = v_sessionkey) 
		AND NOT EXISTS (SELECT 1 
						FROM loader_benchmarkgrades b
						WHERE _fk_sessionkey = v_sessionkey
							AND b.benchmark = s.`key`);
	
END $$

delimiter ;


-- -----------------------------
-- routine loader_getcategories
-- -----------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_getcategories $$

CREATE PROCEDURE loader_getcategories(
/*	
**purpose	 : to retreive data from loader_categories table for a given session key.
**parameters : v_sessionkey cannot be null.
*/
  IN v_sessionkey CHAR(32))
BEGIN  
	
	SELECT  category, treelevel
	FROM loader_categories
	WHERE _fk_sessionkey = v_sessionkey;

END $$

delimiter ;


-- -----------------------------------------
-- routine loader_getnextpublicationversion
-- -----------------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_getnextpublicationversion  $$

CREATE PROCEDURE loader_getnextpublicationversion(
/*	
**purpose	 : takes sessionKey as a parameter and return next version from main tables that match 
			   publisher and subject in loader tables for this sessionKey.
**parameters : v_sessionkey cannot be null.
*/
	IN v_sessionkey CHAR(32))
BEGIN

	-- intilialize variables
	DECLARE v_procname VARCHAR(50);
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);
	DECLARE v_nextversion float;

	SET v_spstartdate = NOW();
	SET v_procname = 'loader_getnextpublicationversion';


	SET v_nextversion = (SELECT MAX(p.version) + 1
						FROM publisher r
							JOIN publication p ON r._key = p._fk_publisher 
						WHERE EXISTS ( SELECT 1
									   FROM loader_publication l
									   WHERE _fk_sessionkey = v_sessionkey
											AND l.publishername = r.`name`
											AND l.`subject` = p.subjectlabel ));

	-- return next available version number that could be entered into the db.	
	SELECT v_nextversion AS nextversion;
	

	SET v_params = CONCAT('sessionkey=', v_sessionkey);
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

END$$

delimiter ;


-- ------------------------------
-- routine loader_getpublication
-- ------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_getpublication $$

CREATE PROCEDURE loader_getpublication(
/*	
**purpose	 : to retreive data from loader_publication table for a given session key.
**parameters : v_sessionkey cannot be null.
*/
  IN v_sessionkey CHAR(32))
BEGIN  
	
	SELECT  publishername, publicationdescription, `subject`, version
	FROM loader_publication
	WHERE _fk_sessionkey = v_sessionkey;

END $$

delimiter ;


-- ------------------------
-- routine loader_getsocks
-- ------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_getsocks $$

CREATE PROCEDURE loader_getsocks(
/*	
**purpose	 : to retreive data from loader_socks table for a given session key.
**parameters : v_sessionkey cannot be null.
*/
  IN v_sessionkey CHAR(32))
BEGIN  
	
	SELECT  knowledgecategory, description
	FROM loader_socks
	WHERE _fk_sessionkey = v_sessionkey;

END $$

delimiter ;


-- ----------------------------
-- routine loader_getstandards
-- ----------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_getstandards $$

CREATE PROCEDURE loader_getstandards(
/*	
**purpose	 : to retreive data from loader_standards table for a given session key.
**parameters : v_sessionkey cannot be null.
*/
  IN v_sessionkey CHAR(32))
BEGIN  

	SELECT  `level` AS standardlevel, `key` AS standardkey, `name` AS standardname, description, shortname
	FROM loader_standards
	WHERE _fk_sessionkey = v_sessionkey;

END $$

delimiter ;


/*################################################################################################################
								**	loader edit methods **
################################################################################################################*/
-- ---------------
-- routine logout
-- ---------------
delimiter $$

DROP PROCEDURE IF EXISTS logout $$

CREATE PROCEDURE logout(
/*	
**purpose	 : to set session end date and call loader procedure to delete data from loader_* table for a given session key.
**parameters : v_sessionkey cannot be null.
*/
	v_sessionkey CHAR(32))
BEGIN  

		-- clear all loader tables for the i/p session key
		CALL loader_clear(v_sessionkey);

		-- set session end date
		UPDATE session	
		SET enddate = NOW()
		WHERE _key = v_sessionkey;
		

END $$

delimiter ;


-- ---------------------
-- routine loader_clear
-- ---------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_clear $$

CREATE PROCEDURE loader_clear(
/*	
**purpose	 : to delete data from loader_* table for a given session key.
**parameters : v_sessionkey cannot be null.
*/
	v_sessionkey CHAR(32))

PROCLABEL: BEGIN  

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_clear';
		  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
	  
		-- clear loader tables
		DELETE FROM loader_initialize WHERE _fk_sessionkey = v_sessionkey;
		DELETE FROM loader_errors WHERE _fk_sessionkey = v_sessionkey;
		DELETE FROM loader_publication WHERE _fk_sessionkey = v_sessionkey;
		DELETE FROM loader_categories WHERE _fk_sessionkey = v_sessionkey;
		DELETE FROM loader_standards WHERE _fk_sessionkey = v_sessionkey;
		DELETE FROM loader_benchmarkgrades WHERE _fk_sessionkey = v_sessionkey;
		DELETE FROM loader_socks WHERE _fk_sessionkey = v_sessionkey;
		DELETE FROM loader_standard_relationship WHERE _fk_sessionkey = v_sessionkey;

	END;
	END IF;

END $$

delimiter ;


-- ------------------------------------
-- routine loader_deletebenchmarkgrade
-- ------------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_deletebenchmarkgrade $$

CREATE PROCEDURE `loader_deletebenchmarkgrade`(
/*	
**purpose	 : to delete data from loader_benchmarkgrades table for a given session key, benchmark and grade.
**parameters : v_sessionkey cannot be null.
			   v_benchmark cannot be null.
			   v_grade cannot be null.
*/
	IN v_sessionkey CHAR(32),
	IN v_benchmark VARCHAR(150), 
	IN v_grade VARCHAR(255))

PROCLABEL:BEGIN
	
	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'loader_deletebenchmarkgrade';
  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
      DELETE 
	  FROM loader_benchmarkgrades 
	  WHERE _fk_sessionkey = v_sessionkey
			AND benchmark = v_benchmark
			AND grade = v_grade;
  END;
  END IF;
 
END $$

delimiter ;


-- ----------------------------------
-- routine loader_editbenchmarkgrade
-- ----------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_editbenchmarkgrade $$

CREATE PROCEDURE loader_editbenchmarkgrade(
/*	
**purpose	 : to update data in loader_benchmarkgrades table for a given session key, benchmark.
**parameters : v_sessionkey cannot be null.
			   v_benchmark cannot be null.
			   v_newgrade cannot be null. all grades are sent as comma seperated. 
*/
	IN v_sessionkey CHAR(32),
	IN v_benchmarkkey VARCHAR(150),
	IN v_newgrade VARCHAR(1000))
	
BEGIN

	IF (EXISTS(SELECT 1 FROM loader_benchmarkgrades WHERE benchmark = v_benchmarkkey AND _fk_sessionkey = v_sessionkey)) THEN
		UPDATE loader_benchmarkgrades
		SET grade = v_newgrade
		WHERE benchmark = v_benchmarkkey
			AND _fk_sessionkey = v_sessionkey;
	ELSE
		INSERT INTO loader_benchmarkgrades VALUES
		(v_benchmarkkey, v_newgrade, v_sessionkey);
	END IF;

	SELECT v_benchmarkkey AS benchmark, v_newgrade AS grade;

END $$

delimiter ;


-- ----------------------------
-- routine loader_editcategory
-- ----------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_editcategory $$

CREATE PROCEDURE loader_editcategory(
/*	
**purpose	 : to update category column data in loader_categories table.
*/
	IN v_sessionkey CHAR(32)
  , IN v_category VARCHAR(50)
  , IN v_treelevel INT
  , IN v_newcategory VARCHAR(50))
	
BEGIN  

	UPDATE loader_categories
	SET category = v_newcategory
	WHERE _fk_sessionkey = v_sessionkey
		AND (v_category IS NULL  OR category = v_category)
		AND treelevel = v_treelevel;	
	
	SELECT v_newcategory AS category, v_treelevel AS treelevel;

END $$

delimiter ;


-- ------------------------
-- routine loader_editsock
-- ------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_editsock $$

CREATE PROCEDURE loader_editsock(
/*	
**purpose	 : to update description and knowledgecategory column data in loader_socks table.
*/
	IN v_sessionkey CHAR(32)
  , IN v_knowledgecategory VARCHAR(50)
  , IN v_description VARCHAR(500)
  , IN v_newknowledgecategory VARCHAR(50)
  , IN v_newdescription VARCHAR(500))
	
BEGIN  

	UPDATE loader_socks
	SET description = v_newdescription
	  , knowledgecategory = v_newknowledgecategory
	WHERE _fk_sessionkey = v_sessionkey
		AND description = v_description
		AND knowledgecategory = v_knowledgecategory;	
	
	SELECT v_newknowledgecategory AS knowledgecategory, v_newdescription AS description;

END $$

delimiter ;


-- ----------------------------
-- routine loader_editstandard
-- ----------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_editstandard $$

CREATE PROCEDURE loader_editstandard(
/*	
**purpose	 : to update description and shortname column data in loader_standards table.
*/
	IN v_sessionkey CHAR(32)
  , IN v_level INT
  , IN v_standardkey VARCHAR(150)
  , IN v_standardname VARCHAR(150)	
  , IN v_newdescription TEXT
  , IN v_newshortname VARCHAR(100))
	
BEGIN  

	UPDATE loader_standards
	SET description = v_newdescription
	  , shortname = v_newshortname
	WHERE _fk_sessionkey = v_sessionkey
		AND `key` = v_standardkey
		AND `level` = v_level
		AND `name` = v_standardname;	

	-- return result set
	SELECT v_level AS standardlevel
		 , v_standardkey AS standardkey
		 , v_standardname AS standardname
		 , v_newdescription AS description
		 , v_newshortname AS shortname;

END $$

delimiter ;


-- ---------------------------
-- routine loader_editversion
-- ---------------------------
delimiter $$

DROP PROCEDURE IF EXISTS loader_editversion $$

CREATE PROCEDURE loader_editversion(
/*	
**purpose	 : to update category column in loader_publication table.
*/
	v_sessionkey CHAR(32)
  , v_publishername VARCHAR(25)
  , v_subject VARCHAR(50)
  , v_newversion INT)
BEGIN  

	UPDATE loader_publication
	SET version = v_newversion
	WHERE _fk_sessionkey = v_sessionkey
		AND (v_publishername IS NULL  OR publishername = v_publishername)
		AND (v_subject IS NULL OR `subject` = v_subject);	
	
	SELECT v_newversion AS newversion;

END $$

delimiter ;


-- ------------------------
-- routine copypublication
-- ------------------------
delimiter $$

DROP PROCEDURE IF EXISTS copypublication $$

CREATE PROCEDURE copypublication(	
/*	
**purpose	 : to copy data from permanent tables into loader_* tables for a given session and publication.
**parameters : v_sessionkey cannot be null.
			   v_publicationkey cannot be null.
*returns error if the user does not have permissions.
*/
  	IN v_sessionkey CHAR(32),
	IN v_publicationkey VARCHAR(20)
)

PROCLABEL: BEGIN  
	
	-- intilialize variables
	DECLARE v_procname VARCHAR(100);
	DECLARE v_counter INT;
	DECLARE v_grade, v_benchmark VARCHAR(200);
	DECLARE v_sessionvalidate BIT;
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);

	SET v_spstartdate = NOW();
	SET v_procname = 'copypublication';
	
	-- check if the user has valid permissions.
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));


	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN

		INSERT INTO loader_publication
		SELECT r.`name`, p.description, p.subjectlabel, p.version, v_sessionkey
		FROM publisher r
			JOIN publication p ON r._key = p._fk_publisher 
		WHERE p._key = v_publicationkey;


		INSERT INTO loader_categories
		SELECT `name`, treelevel, v_sessionkey 
		FROM standard_category 
		WHERE _fk_publication = v_publicationkey;
		

		INSERT INTO loader_standards
		SELECT treelevel, pubkey, `name`, description, shortname, v_sessionkey
		FROM standard 
		WHERE _fk_publication = v_publicationkey
		ORDER BY treelevel, pubkey;


		INSERT INTO loader_socks
		SELECT `name`, description, v_sessionkey 
		FROM sock_drawer 
		WHERE _fk_publication = v_publicationkey;

	/**logic to load data into loader_benchmarkgrades 
	***all grades for a benchmark are loaded are one single comma seperated record*/
		-- creating temporary table to store benchmark grade data fetched from static table.
		DROP TEMPORARY TABLE IF EXISTS tempbenchmark;
		CREATE TEMPORARY TABLE tempbenchmark (id INT AUTO_INCREMENT
											, benchmark VARCHAR(150)
											, grade VARCHAR(255)
											, PRIMARY KEY(id));

		-- creating temporary table to host final result set
		DROP TEMPORARY TABLE IF EXISTS tempbenchmarkresult;
		CREATE TEMPORARY TABLE tempbenchmarkresult (benchmark VARCHAR(150)
												  , grade VARCHAR(1000)
												  , INDEX USING BTREE(benchmark)) ENGINE = MEMORY;
			

		INSERT INTO tempbenchmark(benchmark, grade)
		SELECT s.pubkey AS "benchmark key", sg._fk_gradelevel AS grade
		FROM standard_grade sg
			JOIN standard s ON s._key = sg._fk_standard
		WHERE s._fk_publication = v_publicationkey 
			AND s.treelevel = ( SELECT MAX(treelevel) 
								FROM standard 
								WHERE _fk_publication = v_publicationkey)
		ORDER BY sg._fk_gradelevel, s.pubkey;

		-- initialize counter
		SET v_counter = 1;

		WHILE (v_counter <= (SELECT MAX(id) FROM tempbenchmark)) DO
		BEGIN

			SET v_benchmark = (SELECT benchmark FROM tempbenchmark WHERE id = v_counter);
			SET v_grade	= (SELECT grade FROM tempbenchmark WHERE id = v_counter);

			IF(NOT EXISTS (SELECT benchmark FROM tempbenchmarkresult WHERE benchmark = v_benchmark)) THEN
				INSERT INTO tempbenchmarkresult VALUES (v_benchmark, v_grade);
			ELSE	
				UPDATE tempbenchmarkresult
				SET grade = CONCAT(grade, ',', v_grade)
				WHERE benchmark = v_benchmark;
			END IF;
				
			-- increment counter variable.
			SET v_counter = v_counter + 1;
		
		END;
		END WHILE;

		INSERT INTO loader_benchmarkgrades
		SELECT benchmark, grade, v_sessionkey
		FROM tempbenchmarkresult;

	/* end of loader_benchmarkgrades logic*/

/*
		INSERT INTO loader_standard_relationship
		SELECT sr._fk_standard_a, sr._fk_standard_b, rt.`name` AS relationshiptype, v_sessionkey
		FROM standard_relationship sr, standard s, relationshiptype rt 
		WHERE (s._key = sr._fk_standard_a OR s._key = sr._fk_standard_b) 
			AND sr._fk_relationshiptype = rt._key
			AND s._fk_publication = v_publicationkey;
*/		

		-- check to see if sessionkey exists in table loader_initialize
		-- if exists, update column operation. if not, insert sessionkey details. 
		IF (EXISTS(SELECT 1 FROM loader_initialize WHERE _fk_sessionkey = v_sessionkey)) THEN
			UPDATE loader_initialize
			SET operation = 'copy'
			  , publicationkey = v_publicationkey
			WHERE _fk_sessionkey = v_sessionkey;
		ELSE
			INSERT INTO loader_initialize VALUES	
			(v_sessionkey, 'copy', v_publicationkey);
		END IF;

		SELECT v_sessionkey AS sessionkey;

	END;
	END IF;

	SET v_params = CONCAT('sessionkey=', v_sessionkey, '; publicationkey=', v_publicationkey);
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

END $$

delimiter ;


/*################################################################################################################
								**	add methods **
################################################################################################################*/
-- -----------------
-- routine addgrade
-- -----------------
delimiter $$

DROP PROCEDURE IF EXISTS addgrade $$

CREATE PROCEDURE `addgrade`(
/*	
**purpose	 : to add data to gradelevel table
**parameters : v_sessionkey cannot be null.
			   v_name cannot be null. holds gradename data.
			   v_description is optional.
*returns error record if a grade name is found or if the user does not have permissions.
*/ 
	IN v_sessionkey CHAR(32),
	IN v_name VARCHAR(25),
	IN v_description VARCHAR(200))

PROCLABEL: BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;	  
	SET v_procname = 'addgrade';

	-- check if the user have valid permissions		  	  
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
	  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
	BEGIN		
		IF(LENGTH(v_name) < 1 OR v_name IS NULL) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'invalid grade level', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		IF((SELECT COUNT(*) FROM gradelevel WHERE `name`= v_name) > 0) THEN    
		BEGIN
			CALL _returnerror (v_procname, 'grade key/name already exists in the database', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		INSERT INTO gradelevel(_key, `name`, description) VALUES
		(v_name, v_name, v_description); 	

	  END;
	  END IF;

END $$

delimiter ;


-- ---------------------
-- routine addpublisher
-- ---------------------
delimiter $$

DROP PROCEDURE IF EXISTS addpublisher $$

CREATE PROCEDURE addpublisher (
/*	
**purpose	 : to add data to publisher table
**parameters : v_sessionkey cannot be null.
			   v_pubname cannot be null.
			   v_pubkey cannot be null.
			   v_puburl is optional.
*returns error record if a publisher with either name, key is found or if the user does not have permissions.
*/
	IN v_sessionkey CHAR(32),
	IN v_pubname VARCHAR(25), 
	IN v_pubkey VARCHAR(8),
	IN v_puburl VARCHAR(255))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'addpublisher';
	
	-- check if the user has valid permissions or not
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
	BEGIN
		IF (LENGTH(v_pubkey) < 1 OR v_pubkey IS NULL) THEN  
		BEGIN  
			CALL _returnerror (v_procname, 'invalid publisher key', NULL);  
			LEAVE PROCLABEL;
		END;
		END IF;
 	
		IF (LENGTH(v_pubname) < 1 OR v_pubname IS NULL) THEN   
		BEGIN   
			CALL _returnerror (v_procname, 'invalid publisher name', NULL);
			LEAVE PROCLABEL;
		END;   
		END IF;   
	
		IF ((SELECT COUNT(*) FROM publisher WHERE _key = v_pubkey OR `name` = v_pubname) > 0) THEN    
		BEGIN 
			CALL _returnerror (v_procname, 'publisher key/name already exists in the database', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;
	
		INSERT INTO publisher(_key, `name`, url) VALUES
		(v_pubkey, v_pubname, url); 	

  END;
  END IF;

END $$

delimiter ;


-- -------------------
-- routine addsession
-- -------------------
delimiter $$

DROP PROCEDURE IF EXISTS addsession $$

CREATE PROCEDURE addsession (	
/*	
**purpose	 : to load session related data into session table
**parameters : v_sessionkey cannot be null.
			   v_userid cannot be null.
			   v_openamrole cannot be null.
*returns error record if a publisher with either name, key is found or if the user does not have permissions.
*/
  IN v_sessionkey CHAR(32),	
  IN v_userid VARCHAR(100),
  IN v_openamrole VARCHAR(100))
BEGIN

	-- delete sessions that are more than 30 days old
	DELETE
	FROM `session`
	WHERE DATE_ADD(startdate, INTERVAL 30 DAY) < CURRENT_DATE() 
		OR DATE_ADD(enddate, INTERVAL 30 DAY) < CURRENT_DATE();
	
	-- write session data into table
	INSERT INTO session(`_key`, `user_id`, `startdate`, `enddate`, `openam_role`) VALUES
	(v_sessionkey, v_userid, NOW(), NULL, v_openamrole);

END $$

delimiter ;


-- --------------------------------
-- routine addstandardrelationship
-- --------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS addstandardrelationship $$

CREATE PROCEDURE addstandardrelationship (
/*	
**purpose	 : to add standards relationship type data.
**parameters : v_sessionkey cannot be null.
			   v_standardkeya cannot be null.
			   v_standardkeyb cannot be null.
			   v_relationshiptype cannot be null				
*returns error record if a grade name is found or if the user does not have permissions.
*/ 
	IN v_sessionkey CHAR(32),
	IN v_standardkeya VARCHAR(170),	
	IN v_standardkeyb VARCHAR(170),
	IN v_relationshiptype VARCHAR(15))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_pubkeya, v_pubkeyb VARCHAR(20);
	DECLARE v_sessionvalidate BIT;
  
	SET v_procname = 'addstandardrelationship';

    -- check if the user has valid permissions to add data.
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
	BEGIN 
		SET v_pubkeya = (SELECT _fk_publication FROM standard WHERE _key = v_standardkeya);
		SET v_pubkeyb = (SELECT _fk_publication FROM standard WHERE _key = v_standardkeyb);	
  
		-- validation: standard publication is locked
		IF (EXISTS (SELECT * FROM publication WHERE (_key = v_pubkeya OR _key = v_pubkeyb) AND `status` = 'locked')) THEN    
		BEGIN
			CALL _returnerror (v_procname, 'one or both publication(s) are locked', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		-- validation check to see standard a exists in the table.
		IF(v_pubkeya IS NULL OR LENGTH(v_pubkeya) < 1) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'standard a does not exist', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		-- validation check to see standard b exists in the table.
		IF (v_pubkeyb IS NULL OR LENGTH(v_pubkeyb) < 1) THEN    
		BEGIN
			CALL _returnerror (v_procname, 'standard b does not exist', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		-- validation check to see relationshiptype exists in the table
		IF(NOT EXISTS (SELECT * FROM relationshiptype WHERE _key = v_relationshiptype)) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'relationshiptype does not exist', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		-- validation check to see relationship does not exist in the table.
		IF(EXISTS ( SELECT * FROM standard_relationship 
					WHERE _fk_standard_a = v_standardkeya AND _fk_standard_b = v_standardkeyb AND _fk_relationshiptype = relationshiptype)) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'this relationship already exists', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;


		-- now insert.
		INSERT INTO standard_relationship(_fk_standard_a, _fk_standard_b, _fk_relationshiptype)
		VALUES(v_standardkeya, v_standardkeyb, v_relationshiptype);

  END;
  END IF;

END $$

delimiter ;


-- -------------------
-- routine addsubject
-- -------------------
delimiter $$

DROP PROCEDURE IF EXISTS addsubject $$

CREATE PROCEDURE addsubject (
/*	
**purpose	 : to add data to subject table
**parameters : v_sessionkey cannot be null.
			   v_name cannot be null. holds subjectname data.
			   v_code cannot be null. holds subjectcode data.
*returns error record if a subject with either name, code is found or if the user does not have permissions.
*/ 
	IN v_sessionkey CHAR(32),
	IN v_name VARCHAR(50), 
	IN v_code VARCHAR(10))

PROCLABEL:BEGIN
  
	DECLARE v_sessionvalidate BIT;
	DECLARE v_procname VARCHAR(100);
	SET v_procname = 'addsubject';
  
	-- check if the user have valid permissions
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
	BEGIN  
		IF(LENGTH(v_name) < 1 OR v_name IS NULL) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'invalid subject name', NULL);
			LEAVE PROCLABEL; 
		END;
		END IF;
  
		IF (LENGTH(v_code) < 1 OR v_code IS NULL) THEN   
		BEGIN
			CALL _returnerror (v_procname, 'invalid subject code', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		IF((SELECT COUNT(*) FROM `subject` WHERE `name` = v_name OR `code` = v_code) > 0) THEN   
		BEGIN
			CALL _returnerror (v_procname, 'subject name/code already exists in the database', NULL);
			LEAVE PROCLABEL; 
		END;
		END IF;

		INSERT INTO subject(_key, `name`, `code`) VALUES
		(v_code, v_name, v_code); 	

  END;
  END IF;

END $$

delimiter ;


/*################################################################################################################
								**	get methods **
################################################################################################################*/
-- ----------------------
-- routine getgradelevel
-- ----------------------
delimiter $$

DROP PROCEDURE IF EXISTS getgradelevel $$

CREATE PROCEDURE getgradelevel(
/*	
**purpose	 : to return grade level information or a list of grade levels.
**parameters : v_gradekey is optional.
			   v_publicationkey is optional.
*/ 
	IN v_gradekey VARCHAR(25),
	IN v_publicationkey VARCHAR(20))
BEGIN

	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;  

	IF(v_publicationkey IS NULL) THEN 	
		SELECT _key, `name`, description
		FROM gradelevel 
		WHERE v_gradekey IS NULL OR _key = v_gradekey;
	ELSE 
		SELECT DISTINCT g._key, g.`name`, g.description
		FROM standard_grade sg			
			JOIN standard s ON s._key = sg._fk_standard
			JOIN gradelevel g ON g._key = sg._fk_gradelevel
  		WHERE (v_gradekey IS NULL OR g._key = v_gradekey) 
			AND s._fk_publication = v_publicationkey; 
	END IF;

	-- reset the session attributes so that locking will be used by default.
    SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;  

END $$

delimiter ;


-- -----------------------
-- routine getpublication
-- -----------------------
delimiter $$

DROP PROCEDURE IF EXISTS getpublication $$

CREATE PROCEDURE getpublication(
/*	
**purpose	 : to fetch a publication or a list of publications
**parameters : v_publicationkey is optional.
			   v_publisherkey is optional.
			   v_subjectkey is optional.
*/ 
	IN v_publicationkey VARCHAR(20), 
	IN v_publisherkey VARCHAR(8), 
	IN v_subjectkey VARCHAR(10))
BEGIN
  
	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
  
	SELECT 	p._key, p._fk_publisher, p._date, p.version, p.iscanonical, p.description, p._fk_subject
		, s.`name` AS subjectlabel
		, p.url, p.`status` AS publicationstatus
		, pr.`name` AS publishername
	FROM publication p
		JOIN `subject` s ON p._fk_subject = s._key
		JOIN publisher pr ON p._fk_publisher = pr._key
	WHERE (v_publicationkey IS NULL OR p._key = v_publicationkey)
		AND (v_publisherkey IS NULL OR p._fk_publisher = v_publisherkey)
		AND (v_subjectkey IS NULL OR p._fk_subject = v_subjectkey);
    
	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ;


-- ----------------
-- routine getsocks
-- ----------------
delimiter $$

DROP PROCEDURE IF EXISTS getsocks $$

CREATE PROCEDURE getsocks (
/*      
**purpose        : to fetch data from sock drawer table.
*/ 
        IN v_publicationkey VARCHAR(20))
                                
BEGIN

        -- set the session attributes so that we do not lock up the table.
        SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

        SELECT _key, `name`, description, _fk_publication
        FROM sock_drawer
        WHERE v_publicationkey IS NULL 
                 OR _fk_publication = v_publicationkey;

        -- reset the session attributes so that locking will be used by default.
        SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ;


-- -------------------------------------------
-- routine getpublicationbysubjectbypublisher
-- -------------------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS `getpublicationbysubjectbypublisher` $$

CREATE PROCEDURE `getpublicationbysubjectbypublisher`(
/*	
**purpose	 : to fetch publication data filterd by subject and publisher keys.
**parameters : v_publisherkey cannot be null.
			   v_subjectkey cannot be null.
*/ 
	IN v_publisherkey VARCHAR(20)
  , IN v_subjectkey VARCHAR(20))
BEGIN

	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

	SELECT  DISTINCT _key AS publicationkey
		 , description AS publicationdescription
		 , version AS publicationversion
		 , _fk_publisher AS publisher
		 , _date
		 , iscanonical
		 , _fk_subject AS `subject`
		 , subjectlabel
		 , `status`
		 , url
	FROM publication
	WHERE _fk_publisher = v_publisherkey
		AND _fk_subject = v_subjectkey;

	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ;


-- ---------------------
-- routine getpublisher
-- ---------------------
delimiter $$

DROP PROCEDURE IF EXISTS getpublisher $$

-- get a publisher or a list of publishers
-- @param publisherkey key into the publisher table. may be null, in which case it returns all publishers
CREATE PROCEDURE getpublisher (
/*	
**purpose	 : to fetch data from publisher table.
**parameters : v_publisherkey is optional.
*/ 
	IN v_publisherkey VARCHAR(20))
BEGIN

	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

	SELECT _key, `name`, url 
	FROM publisher 
	WHERE v_publisherkey IS NULL 
		OR _key = v_publisherkey;

	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ;


-- --------------------
-- routine getstandard
-- --------------------
delimiter $$

DROP PROCEDURE IF EXISTS getstandard $$

CREATE PROCEDURE getstandard (
/*	
**purpose	 : to fetch list of standards
**parameters : v_publicationkey is optional.
			   v_gradekey is optional.
			   v_gradeagnostic is optional.
*/ 
	IN v_publicationkey VARCHAR(20),
	IN v_gradekey VARCHAR(20),
	IN v_gradeagnostic BIT)
BEGIN

	DECLARE v_procname VARCHAR(50);
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);

	SET v_spstartdate = NOW();
	SET v_procname = 'getstandard';


	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED ;
	
	SELECT _key, `name`, _fk_parent, _fk_publication, description, treelevel, sg._fk_gradelevel, s.pubkey, s.shortname
	FROM standard s
		LEFT JOIN standard_grade sg ON s._key = sg._fk_standard 
	WHERE (v_publicationkey IS NULL OR s._fk_publication = v_publicationkey)
		AND (v_gradekey IS NULL OR sg._fk_gradelevel = v_gradekey OR (sg._fk_gradelevel IS NULL AND  v_gradeagnostic = 1));		
  
	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ ; 


	SET v_params = CONCAT('publicationkey=', COALESCE(v_publicationkey, 'NULL'), ';gradekey=', COALESCE(v_gradekey, 'NULL'), ';gradeagnostic=', COALESCE(v_gradeagnostic, 'NULL'));
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

	
END $$

delimiter ;


-- ---------------------------
-- routine getstandardcategory
-- ---------------------------
delimiter $$

DROP PROCEDURE IF EXISTS getstandardcategory $$

CREATE PROCEDURE getstandardcategory(
/*	
**purpose	 : to fetch data from standard category table.
*/ 
	IN v_publicationkey VARCHAR(20))
                                
BEGIN

	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

	SELECT _fk_publication, `name`, treelevel
	FROM standard_category
	WHERE v_publicationkey IS NULL 
		 or _fk_publication = v_publicationkey;

	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ;


-- ---------------------------------
-- routine getstandardrelationships
-- ---------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS getstandardrelationships $$

CREATE PROCEDURE getstandardrelationships(
/*	
**purpose	 : to fetch list of standard relationships between two standards
**parameters : v_pubkeya cannot be null.
			   v_pubkeyb cannot be null.
*/
	IN v_pubkeya VARCHAR(20),
	IN v_pubkeyb VARCHAR(20))
BEGIN
  
	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
  
	SELECT _fk_standard_a, _fk_standard_b, _fk_relationshiptype 
	FROM standard_relationship sr
		JOIN standard a ON a._key = sr._fk_standard_a
		JOIN standard b ON b._key = sr._fk_standard_b
	WHERE a._fk_publication = v_pubkeya 
		AND b._fk_publication = v_pubkeyb;

	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ;


-- -------------------
-- routine getsubject
-- -------------------
delimiter $$

DROP PROCEDURE IF EXISTS getsubject $$

CREATE PROCEDURE getsubject(
/*	
**purpose	 : to fetch a subject or a list of subjects
**parameters : v_subjectkey cannot be null.
*/
	IN v_subjectkey VARCHAR(20))
BEGIN

	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

	SELECT  _key AS subjectkey, `name` AS subjectlabel, `code` AS subjectcode
	FROM `subject` 
	WHERE v_subjectkey IS NULL
		OR _key = v_subjectkey;

	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ;


-- ------------------------------
-- routine getsubjectbypublisher
-- ------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS getsubjectbypublisher $$

CREATE PROCEDURE `getsubjectbypublisher`(
/*	
**purpose	 : to fetch distinct list of subjects for a given publisher.
**parameters : v_publisherkey cannot be null.
*/
	IN v_publisherkey VARCHAR(20))
BEGIN

	-- set the session attributes so that we do not lock up the table.
	SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

	SELECT  DISTINCT s._key AS subjectkey, s.`name` AS subjectlabel, s.`code` as subjectcode
	FROM `subject` s
		JOIN publication AS p ON p._fk_subject = s._key
	WHERE p._fk_publisher = v_publisherkey;

	-- reset the session attributes so that locking will be used by default.
	SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 

END $$

delimiter ; 


/*################################################################################################################
								**	edit & fetch methods **
################################################################################################################*/
-- --------------------------
-- routine createpublication
-- --------------------------
delimiter $$

DROP PROCEDURE IF EXISTS createpublication $$

CREATE PROCEDURE createpublication (
/*	
**purpose	 : to create/add new publication 
*/
	IN v_sessionkey CHAR(32),
	IN v_publicationkey VARCHAR(20), 
	IN v_publisherkey VARCHAR(8), 
	IN v_datepublished DATETIME, 
	IN v_version FLOAT, 
	IN v_iscannonical BIT, 
	IN v_description VARCHAR(255), 
	IN v_subjectkey VARCHAR(4), 
	IN v_subjectlabel VARCHAR(25),
	IN v_url VARCHAR(255))

 PROCLABEL:BEGIN
 
	DECLARE v_sessionvalidate BIT;

	SET v_sessionvalidate = (SELECT validateaccess(sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END; 
	ELSE 
	
		INSERT INTO publication(_key, _fk_publisher, _date, version, iscanonical, description, _fk_subject, subjectlabel, url) VALUES
		(keypublication, publisherkey, datepublished, version, iscannonical, description, subjectkey, subjectlabel, url);

	END IF;
	
END $$

delimiter ;


-- --------------------------
-- routine exportpublication
-- --------------------------
delimiter $$

DROP PROCEDURE IF EXISTS exportpublication $$

CREATE PROCEDURE exportpublication(
/*	
**purpose	 : to export a publication data.
*/
	IN v_publicationkey VARCHAR(20))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	SET v_procname = 'exportpublication';


	SELECT r.`name` AS publishername, p.description AS publicationdescription, p.subjectlabel AS `subject`, p.version
	FROM publisher r, publication p
	WHERE r._key = p._fk_publisher 
		AND p._key = v_publicationkey;

	SELECT `name` AS category, treelevel 
	FROM standard_category 
	WHERE _fk_publication = v_publicationkey;

	SELECT treelevel AS `level`, pubkey AS `key`, `name`, description, shortname
	FROM standard 
	WHERE _fk_publication = v_publicationkey 
	ORDER BY treelevel, pubkey;

	SELECT `name` AS `knowledge category`, description 
	FROM sock_drawer 
	WHERE _fk_publication = v_publicationkey;

	SELECT s.pubkey AS `benchmark key`, sg._fk_gradelevel AS grade 
	FROM standard_grade sg
		JOIN standard s ON s._key = sg._fk_standard
	WHERE s._fk_publication = v_publicationkey 
		AND s.treelevel = ( SELECT MAX(treelevel) 
							FROM standard 
							WHERE _fk_publication = v_publicationkey)
	ORDER BY sg._fk_gradelevel, s.pubkey;

	SELECT sr._fk_standard_a AS v_standardkeya, sr._fk_standard_b AS v_standardkeyb, rt.`name` AS relationshiptype
	FROM standard_relationship sr
		JOIN standard s ON s._key = sr._fk_standard_a OR s._key = sr._fk_standard_b 
		JOIN relationshiptype rt ON rt._key = sr._fk_relationshiptype
	WHERE s._fk_publication = v_publicationkey;

END $$

delimiter ;

-- ------------------------
-- routine editpublication
-- ------------------------
delimiter $$

DROP PROCEDURE IF EXISTS editpublication $$

CREATE PROCEDURE `editpublication`(
	IN v_sessionkey CHAR(32),
	IN v_publicationkey VARCHAR(25))
PROCLABEL: BEGIN
	
	-- intilialize variables
	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	DECLARE v_spstartdate, v_spenddate DATETIME;
	DECLARE v_params VARCHAR(5000);

	SET v_spstartdate = NOW();
	SET v_procname = 'editpublication';

	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));

	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
    BEGIN
		INSERT INTO loader_publication
		SELECT r.`name`, p.description, p.subjectlabel, p.version, v_sessionkey
		FROM publisher r, publication p
		WHERE r._key = p._fk_publisher AND p._key = v_publicationkey;

		INSERT INTO loader_categories
		SELECT `name`, treelevel, v_sessionkey 
		FROM standard_category 
		WHERE _fk_publication = v_publicationkey;
		
		INSERT INTO loader_standards
		SELECT treelevel, pubkey, `name`, description, shortname, v_sessionkey
		FROM standard 
		WHERE _fk_publication = v_publicationkey
		ORDER BY treelevel, pubkey;

		INSERT INTO loader_socks
		SELECT `name`, description, v_sessionkey 
		FROM sock_drawer WHERE _fk_publication = v_publicationkey;

		INSERT INTO loader_benchmarkgrades
		SELECT s.pubkey AS "benchmark key", sg._fk_gradelevel AS grade, v_sessionkey 
		FROM standard_grade sg, standard s 
		WHERE s._key = sg._fk_standard 
			AND s._fk_publication = v_publicationkey 
			AND s.treelevel = ( SELECT MAX(treelevel) 
								FROM standard 
								WHERE _fk_publication = v_publicationkey)
		ORDER BY sg._fk_gradelevel, s.pubkey;

/*
		INSERT INTO loader_standard_relationship
		SELECT sr._fk_standard_a, sr._fk_standard_b, rt.`name` AS relationshiptype, v_sessionkey
		FROM standard_relationship sr, standard s, relationshiptype rt 
		WHERE (s._key = sr._fk_standard_a OR s._key = sr._fk_standard_b) 
			AND sr._fk_relationshiptype = rt._key
			AND s._fk_publication = v_publicationkey;
*/
		
		-- check to see if sessionkey exists in table loader_initialize
		-- if exists, update column operation. if not, insert sessionkey details. 
		IF (EXISTS(SELECT 1 FROM loader_initialize WHERE _fk_sessionkey = v_sessionkey)) THEN
			UPDATE loader_initialize
			SET operation = 'edit'
			  , publicationkey = v_publicationkey
			WHERE _fk_sessionkey = v_sessionkey;
		ELSE
			INSERT INTO loader_initialize VALUES	
			(v_sessionkey, 'edit', v_publicationkey);
		END IF;


	END;
	END IF;

	SET v_params = CONCAT('sessionkey=', v_sessionkey, '; publicationkey=', v_publicationkey);
	SET v_spenddate = NOW();

	CALL writelatencylog(v_procname, v_params, v_spstartdate, v_spenddate);

END $$

delimiter ;


-- --------------------------
-- routine importpublication
-- --------------------------
delimiter $$

DROP PROCEDURE IF EXISTS importpublication $$

CREATE PROCEDURE `importpublication`(
	IN v_sessionkey CHAR(32))
PROCLABEL: BEGIN  
	
	-- intilialize variables
	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;

	SET v_procname = 'importpublication';
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));

	IF(v_sessionvalidate = 0) THEN 
	  BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	  END;    
	ELSE
    BEGIN
		-- check to see if sessionkey exists in table loader_initialize
		-- if exists, update column operation. if not, insert sessionkey details.     
		IF (EXISTS(SELECT 1 FROM loader_initialize WHERE _fk_sessionkey = v_sessionkey)) THEN
			UPDATE loader_initialize
			SET operation = 'import'
			  , publicationkey = NULL
			WHERE _fk_sessionkey = v_sessionkey;
		ELSE
			INSERT INTO loader_initialize VALUES	
			(v_sessionkey,'import', NULL);
		END IF;
    
    END;
    END IF;

END $$

delimiter ;


-- ---------------------------------
-- routine partialimportpublication
-- ---------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS partialimportpublication $$

CREATE PROCEDURE `partialimportpublication`(
	IN v_sessionkey CHAR(32))

PROCLABEL: BEGIN  
	
	-- intilialize variables
	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;

	SET v_procname = 'partialimportpublication';
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));

	IF(v_sessionvalidate = 0) THEN 
	  BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	  END;    
	ELSE
    BEGIN
		CALL loader_clear(v_sessionkey);

		-- check to see if sessionkey exists in table loader_initialize
		-- if exists, update column operation. if not, insert sessionkey details.     
		IF (EXISTS(SELECT 1 FROM loader_initialize WHERE _fk_sessionkey = v_sessionkey)) THEN
			UPDATE loader_initialize
			SET operation = 'partialimport'
			  , publicationkey = NULL
			WHERE _fk_sessionkey = v_sessionkey;
		ELSE
			INSERT INTO loader_initialize VALUES	
			(v_sessionkey,'partialimport', NULL);
		END IF;
    
    END;
    END IF;

END $$

delimiter ;


-- --------------------------------
-- routine modifypublicationstatus
-- --------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS modifypublicationstatus $$

CREATE PROCEDURE modifypublicationstatus(	
/*	
**purpose	 : to update the status of publication
**parameters : v_sessionkey cannot be null.
			   v_pubkey represents publication key and it cannot be a null value.
			   v_pubstatus represents publication status. 
*/
	IN v_sessionkey CHAR(32),
	IN v_pubkey VARCHAR(20), 
	IN v_pubstatus VARCHAR(50))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'modifypublicationstatus';

	-- check to see if the user has valid permissions to perform this update operation
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));
  
	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;     
	ELSE
	BEGIN
		-- validation: publication exists
		IF(NOT EXISTS (SELECT * FROM publication WHERE _key = v_pubkey)) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'publication does not exist', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;
  
		UPDATE publication 
		SET `status` = v_pubstatus 
		WHERE _key = v_pubkey;

  END;
  END IF;
		
END $$

delimiter ;


-- ------------------------------------
-- routine removestandardrelationship
-- ------------------------------------
delimiter $$

DROP PROCEDURE IF EXISTS removestandardrelationship $$

CREATE PROCEDURE removestandardrelationship (
/*	
**purpose	 : to remove standards relationship
**parameters : v_sessionkey cannot be null.
			   v_standardkeya cannot be null.
			   v_standardkeyb cannot be a null value.
			   v_relationshiptype cannot be null. 
*/	
	IN v_sessionkey CHAR(32),
	IN v_standardkeya VARCHAR(170),	
	IN v_standardkeyb VARCHAR(170),
	IN v_relationshiptype VARCHAR(15))

PROCLABEL:BEGIN

	DECLARE v_procname VARCHAR(100);
	DECLARE v_pubkeya, v_pubkeyb VARCHAR(20);
	DECLARE v_sessionvalidate BIT;
	SET v_procname = 'removestandardrelationship';

	-- check to see if the user has valid permissions to perform this update operation
	SET v_sessionvalidate = (SELECT validateaccess(v_sessionkey, 'write'));

	IF(v_sessionvalidate = 0) THEN 
	BEGIN
		CALL _returnerror (v_procname, 'invalid access', NULL);
		LEAVE PROCLABEL;
	END;    
	ELSE
	BEGIN

		SET v_pubkeya = (SELECT _fk_publication FROM standard WHERE _key = v_standardkeya);
		SET v_pubkeyb = (SELECT _fk_publication FROM standard WHERE _key = v_standardkeyb);	

		-- validation: standard publication is locked
		IF (EXISTS (SELECT * FROM publication WHERE (_key = v_pubkeya OR _key = v_pubkeyb) AND `status` = 'locked')) THEN    
		BEGIN
			CALL _returnerror (v_procname, 'one or both publication(s) are locked', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		-- validation: standard must exists
		IF(v_pubkeya IS NULL OR LENGTH(v_pubkeya) < 1) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'standard a does not exist', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		IF (v_pubkeya IS NULL OR LENGTH(v_pubkeya) < 1) THEN    
		BEGIN
			CALL _returnerror (v_procname, 'standard b does not exist', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		-- validation: relationship exists
		IF(NOT EXISTS (SELECT * FROM relationshiptype WHERE _key = v_relationshiptype)) THEN  
		BEGIN
			CALL _returnerror (v_procname, 'relationship type does not exist', NULL);
			LEAVE PROCLABEL;
		END;
		END IF;

		-- now delete 
		-- todo sb: can there be more than one kind of relationship between two standards?
		DELETE FROM standard_relationship 
		WHERE _fk_standard_a = v_standardkeya 
			AND _fk_standard_b = v_standardkeyb 
			AND _fk_relationshiptype = v_relationshiptype;		

	END;
	END IF;	

END $$

delimiter ;


-- ---------------------
-- routine _returnerror
-- ---------------------
delimiter $$

DROP PROCEDURE IF EXISTS _returnerror $$

CREATE PROCEDURE _returnerror (
/*	
**purpose	 : to return a single record with status, reason, context and arguments as columns.
**parameters : v_procname: represents name of a stored procedure. this will be returned under the column "context".
			   v_msg: represents defaulted message. this will be returned as column reason.
			   v_argstring: represents a comma separated list of values that will be returned as is.
*/	
	IN v_procname VARCHAR(200), 
	IN v_msg VARCHAR(200), 
	IN v_argstring VARCHAR(1000))
BEGIN

	SELECT 'failed' AS `status`
		 , v_msg AS reason
		 , v_procname AS `context`
		 , v_argstring AS argstring
		 , CASE WHEN v_argstring IS NULL THEN NULL ELSE ',' END AS `delimiter`;

END$$

delimiter ;


-- ------------------------
-- routine _returnsuccess
-- ------------------------
delimiter $$

DROP PROCEDURE IF EXISTS _returnsuccess $$

CREATE PROCEDURE _returnsuccess(
/*	
**purpose	 : to return a single record with status, reason, context and arguments as columns.
**parameters : v_procname: represents name of a stored procedure. this will be returned under the column "context".
			   v_msg: represents defaulted message. this will be returned as column reason.
			   v_argstring: represents a comma separated list of values that will be returned as is.
*/	
	IN v_procname VARCHAR(200), 
	IN v_msg VARCHAR(200), 
	IN v_argstring VARCHAR(1000))
BEGIN

	SELECT 'success' AS `status`
		 , v_msg AS reason
		 , v_procname AS `context`
		 , v_argstring AS argstring
		 , CASE WHEN v_argstring IS NULL THEN NULL ELSE ',' END AS `delimiter`;

END $$

delimiter ;


-- --------------------
-- routine _buildtable
-- --------------------
delimiter $$

DROP PROCEDURE IF EXISTS _buildtable $$

CREATE PROCEDURE _buildtable(
	IN v_list TEXT, 
	IN v_delimit CHAR)
BEGIN  
  
	DECLARE v_modifiedlist TEXT;
	DECLARE v_searchposition, v_commaposition, v_recordcount INT;
  
	-- lets first create a temporary table.
	DROP TEMPORARY TABLE IF EXISTS resulttable;
	CREATE TEMPORARY TABLE resulttable (idx INT, record VARCHAR(255));
  
	-- first remove the leading and trailing delimiters.
	SELECT TRIM(BOTH v_delimit FROM v_list) INTO V_MODIFIEDLIST;
  
	-- now start processing.
	SET v_searchposition = 1;
	SET v_recordcount = 1;
	SET v_commaposition = LOCATE(v_delimit, v_modifiedlist, v_searchposition);
  
	WHILE (v_commaposition > 1) DO
	BEGIN
		INSERT INTO resulttable (idx, record) VALUES(v_recordcount, SUBSTRING(v_modifiedlist FROM v_searchposition FOR (v_commaposition - v_searchposition)));
    
		-- increament index.
		SET v_recordcount = v_recordcount + 1;

		-- move pointer.
		SET v_searchposition = v_commaposition + 1;

		IF (v_searchposition = LENGTH(v_modifiedlist)) THEN    
			-- set v_commaposition to a negative number so that the while loop terminates.
			SET v_commaposition = 0;
		ELSE  
			SET v_commaposition = LOCATE(v_delimit, v_modifiedlist, v_searchposition);
		END IF;
	END;
	END WHILE;

	-- now we need to extract the residual string and insert it. this will take care of cases like "a,b" where we 
	-- v_searchposition is not set to 3.
	INSERT INTO resulttable (idx, record) VALUES(v_recordcount, SUBSTRING(v_modifiedlist FROM v_searchposition));
     
	-- select from the temporary table to return v_result set.
	INSERT INTO temp_funcbuildtable
	SELECT idx, record 
	FROM resulttable;


END $$

delimiter ;


-- ---------------------
-- routine _buildtable1
-- ---------------------
delimiter $$

DROP PROCEDURE IF EXISTS _buildtable1 $$

CREATE PROCEDURE _buildtable1(
	IN v_list TEXT, 
	IN v_delimit CHAR)
BEGIN  

	DECLARE v_modifiedlist TEXT;
	DECLARE v_searchposition, v_commaposition, v_recordcount INT; 
	DECLARE v_currentrecord VARCHAR(255);
 
	-- lets first create a temporary table.
	DROP TEMPORARY TABLE IF EXISTS resulttable;
	CREATE TEMPORARY TABLE resulttable (idx INT, record VARCHAR(255));
  
	-- first remove the leading and trailing delimiters.
	SET v_modifiedlist = (SELECT TRIM(BOTH v_delimit FROM v_list));
  
	-- now start processing.
	SET v_searchposition = 1;
	SET v_recordcount = 1;
	SET v_currentrecord = '';
	SET v_commaposition = LOCATE(v_delimit, v_modifiedlist, v_searchposition);
  
	WHILE (v_commaposition > 1) DO
	BEGIN
		IF (v_recordcount > 1) THEN   
			SET v_currentrecord = CONCAT(v_currentrecord, '|');
		END IF;
    
		SET v_currentrecord = (CONCAT(v_currentrecord, SUBSTRING(v_modifiedlist FROM v_searchposition FOR (v_commaposition - v_searchposition))));

		INSERT INTO resulttable (idx, record) VALUES(v_recordcount, v_currentrecord);
    
		-- increament index.
		SET v_recordcount = v_recordcount + 1;
    
		-- move pointer.
		SET v_searchposition = v_commaposition + 1;
		IF (v_searchposition = LENGTH(v_modifiedlist)) THEN    
			-- set v_commaposition to a negative number so that the while loop terminates.
			SET v_commaposition = 0;
		ELSE  
			SET v_commaposition = LOCATE(v_delimit, v_modifiedlist, v_searchposition);
		END IF;
	END;
	END WHILE;

	-- now we need to extract the residual string and insert it. this will take care of cases like "a,b" where we 
	-- v_searchposition is not set to 3.
	IF (v_recordcount > 1) THEN  
		SET v_currentrecord = CONCAT(v_currentrecord, '|');
	END IF;
   
	SET v_currentrecord = CONCAT(v_currentrecord, SUBSTRING(v_modifiedlist FROM v_searchposition));
	
	INSERT INTO resulttable (idx, record) VALUES(v_recordcount, v_currentrecord);
     
	-- select from the temporary table to return v_result set.
	INSERT INTO temp_funcbuildtable1
	SELECT idx, record FROM resulttable;

  
END $$

delimiter ;


-- ------------------------
-- routine writelatencylog
-- ------------------------
delimiter $$

DROP PROCEDURE IF EXISTS writelatencylog $$

CREATE PROCEDURE writelatencylog (
	IN v_procname VARCHAR(200),
	IN v_params VARCHAR(5000),
	IN v_startdate DATETIME,
	IN v_enddate DATETIME)
BEGIN
	
	-- check config
	IF (EXISTS (SELECT loglatency FROM logconfig WHERE loglatency = 1)) THEN
		-- if logging enabled, write latency log entry		
		INSERT INTO latencylog(procname, params, startdate, enddate)
		VALUES (v_procname, v_params, v_startdate, v_enddate);
	END IF;

END $$

delimiter ;


/*################################################################################################################
									**	functions **
################################################################################################################*/
-- --------------------
-- routine _lastindex
-- --------------------
delimiter $$

DROP FUNCTION IF EXISTS _lastindex $$

CREATE FUNCTION _lastindex(
	v_str VARCHAR(1000),
	v_chr CHAR) 
RETURNS INT(11) DETERMINISTIC
BEGIN

	RETURN LENGTH(v_str) - SUBSTRING_INDEX(v_chr, REVERSE(v_str), 1 + 1);

END $$

delimiter ;


-- ------------------------------------------
-- routine loader_validatepublicationversion
-- ------------------------------------------
delimiter $$

DROP FUNCTION IF EXISTS loader_validatepublicationversion $$

CREATE FUNCTION loader_validatepublicationversion(
	v_sessionkey CHAR(32))	
RETURNS VARCHAR(20) DETERMINISTIC
BEGIN  

	DECLARE v_result VARCHAR(20);

	-- verifies if the version of the publication in loader_publication matches 
	-- existing version in the publication table.
	IF (EXISTS(SELECT * FROM loader_publication l WHERE l._fk_sessionkey = v_sessionkey
													AND EXISTS (SELECT *
																FROM publication p
																WHERE p._fk_publisher = l.publishername
																	AND p.description = l.publicationdescription
																	AND p.subjectlabel = l.`subject`
																	AND p.version = l.version)))
	THEN
		SET v_result = 'match';
	ELSE
		SET v_result = 'no-match';
	END IF;

	RETURN v_result;

END $$

delimiter ;


-- ---------------------------
-- routine makepublicationkey
-- ---------------------------
delimiter $$

DROP FUNCTION IF EXISTS makepublicationkey $$

CREATE FUNCTION makepublicationkey(
	v_publisher VARCHAR(10), 
	v_subject VARCHAR(10), 
	v_publicationversion FLOAT)
RETURNS VARCHAR(20) DETERMINISTIC
BEGIN

    DECLARE v_result VARCHAR(20);
    SET v_result = CONCAT(CONCAT(CONCAT(v_publisher, '-'), CONCAT(v_subject, '-v')), CAST(v_publicationversion AS CHAR(5)));

    RETURN v_result;
END$$

delimiter ;


-- ---------------------
-- routine makesockskey
-- ---------------------
delimiter $$

DROP FUNCTION IF EXISTS makesockskey $$

CREATE FUNCTION makesockskey(
-- create a function to return a ":" delimited string from two strings
	v_publicationkey VARCHAR(20), 
	v_knowledgecategory VARCHAR(100))
RETURNS VARCHAR(170) DETERMINISTIC
BEGIN

	DECLARE v_result VARCHAR(170);
	SET v_result = CONCAT( CONCAT(v_publicationkey, ':'), v_knowledgecategory);
	
	RETURN v_result;

END $$

delimiter ;


-- ------------------------
-- routine makestandardkey
-- ------------------------
delimiter $$

DROP FUNCTION IF EXISTS makestandardkey $$

CREATE FUNCTION makestandardkey(
	v_publicationkey VARCHAR(20), 
	v_standardname VARCHAR(150))
RETURNS VARCHAR(170) DETERMINISTIC
BEGIN

	DECLARE v_result VARCHAR(170);
	SET v_result = CONCAT( CONCAT(v_publicationkey, ':'), TRIM(BOTH ' ' FROM v_standardname));

	RETURN v_result;

END $$

delimiter ;


-- -----------------------------
-- routine publicationgradelist
-- -----------------------------
delimiter $$

DROP FUNCTION IF EXISTS publicationgradelist $$

CREATE FUNCTION publicationgradelist(
	v_pubkey VARCHAR(20)) 
RETURNS VARCHAR(250) DETERMINISTIC
BEGIN

    DECLARE v_result VARCHAR(250);
    DECLARE v_grade VARCHAR(25);

	CREATE TEMPORARY TABLE tbl(grade VARCHAR(25));

	INSERT INTO tbl(grade)
    SELECT DISTINCT r._fk_gradelevel
    FROM  standard_grade r
		JOIN standard s ON r._fk_standard = s._key
    WHERE s._fk_publication = v_pubkey;

    WHILE (EXISTS (SELECT * FROM tbl)) 
	DO
        SET v_grade = (SELECT grade FROM tbl LIMIT 1);
	
        DELETE FROM tbl WHERE grade = v_grade;

        IF (v_result IS NULL) THEN
			SET v_result = v_grade;
        ELSE
			SET v_result = v_result + ',' + v_grade;
		END IF;

    END WHILE;

    RETURN v_result;

END $$

delimiter ;


-- --------------------------
-- routine standardgradelist
-- --------------------------
delimiter $$

DROP FUNCTION IF EXISTS standardgradelist $$

CREATE FUNCTION standardgradelist(
	v_standardkey VARCHAR(200)) 
RETURNS VARCHAR(250) DETERMINISTIC
BEGIN

	DECLARE v_result VARCHAR(250);
	DECLARE v_grade VARCHAR(25);

	CREATE TEMPORARY TABLE tbl(grade VARCHAR(25)); 

    INSERT INTO tbl (grade)
    SELECT DISTINCT r._fk_gradelevel
    FROM standard_grade r
    WHERE r._fk_standard = v_standardkey ;

    WHILE (EXISTS (SELECT * FROM tbl)) 
	DO
        SET v_grade = (SELECT grade FROM tbl LIMIT 1);
        
		DELETE FROM tbl WHERE grade = v_grade;

        IF (v_result IS NULL) THEN
			SET v_result = v_grade;
        ELSE
			SET v_result = v_result + ',' + v_grade;
		END IF;

    END WHILE;

    RETURN v_result;

END $$

delimiter ;


-- ---------------------------
-- routine generatesessionkey
-- ---------------------------
delimiter $$

DROP FUNCTION IF EXISTS generatesessionkey $$

CREATE FUNCTION generatesessionkey()
	RETURNS CHAR(32) DETERMINISTIC
BEGIN

	DECLARE v_result CHAR(32);
	SET v_result = (SELECT UUID());

	RETURN v_result;

END $$

delimiter ;


-- -----------------------
-- routine validateaccess
-- -----------------------
delimiter $$

DROP FUNCTION IF EXISTS validateaccess $$

CREATE FUNCTION validateaccess(
	v_sessionkey CHAR(32),
	v_csrole VARCHAR(10)) 
RETURNS BIT DETERMINISTIC
BEGIN

	DECLARE temp_csrole VARCHAR(10);
	DECLARE v_result BIT;

	SELECT r.cs_role
	INTO TEMP_CSROLE
	FROM `session` s
		 LEFT JOIN role_map r ON r.openam_role = s.openam_role
	WHERE s._key = v_sessionkey;

	IF (v_csrole = temp_csrole) THEN	
		SET v_result = 1;
	ELSE
		SET v_result = 0;
	END IF;

	RETURN v_result;

END $$

delimiter ;