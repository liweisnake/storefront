INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
1, 'Enterprise application', 'Enterprise application'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
2, 'game', 'game'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
3, 'small business application', 'small business application'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
4, 'browse', 'browse'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
5, 'sports', 'sports'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
6, 'music', 'music'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
7, 'finicial', 'finicial'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
8, 'communication', 'communication'); 
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
9, 'books', 'books');
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
10, 'news', 'news');
INSERT INTO Category ( ID, NAME, DESCRIPTION ) VALUES ( 
11, 'health', 'health');
commit;
 
INSERT INTO Country ( ID, NAME ) VALUES ( 
1, 'China'); 
INSERT INTO Country ( ID, NAME ) VALUES ( 
2, 'Canada'); 
INSERT INTO Country ( ID, NAME ) VALUES ( 
3, 'France'); 
INSERT INTO Country ( ID, NAME ) VALUES ( 
4, 'India'); 
commit;
 
INSERT INTO Language ( ID, NAME, LOCALE ) VALUES ( 
1, 'Chinese', 'zh'); 
INSERT INTO Language ( ID, NAME, LOCALE ) VALUES ( 
2, 'English', 'en'); 
commit;
 
INSERT INTO Platform( ID, NAME, DESCRIPTION ) VALUES ( 
1, 'Palm OS', 'Palm OS'); 
INSERT INTO Platform( ID, NAME, DESCRIPTION ) VALUES ( 
2, 'Android', 'Android'); 
INSERT INTO Platform( ID, NAME, DESCRIPTION ) VALUES ( 
3, 'Windows', 'Windows'); 
INSERT INTO Platform( ID, NAME, DESCRIPTION ) VALUES ( 
4, 'Linux', 'Linux'); 
INSERT INTO Platform( ID, NAME, DESCRIPTION ) VALUES ( 
5, 'J2ME', 'J2ME'); 
commit;


INSERT INTO SYSTEMCONFIG ( ID, CONFIGKEY, VALUE ) VALUES ( 
1, 'default.new.arrival.due.days', '0'); 
INSERT INTO SYSTEMCONFIG ( ID, CONFIGKEY, VALUE ) VALUES ( 
2, 'default.commission.rate.MCD', '0'); 
INSERT INTO SYSTEMCONFIG ( ID, CONFIGKEY, VALUE ) VALUES ( 
3, 'default.pid.sbm', 'SBM-PID'); 

commit;