CREATE TABLE APP_NOTIFICATION_DEVICES (PIPE_ID BIGINT NOT NULL, DEVICE_ID VARCHAR(255));
CREATE TABLE CONTENT_SELECTOR (CONTENT_SELECTOR_ID BIGINT NOT NULL AUTO_INCREMENT, LABEL VARCHAR(255), CONTENT_SELECTOR VARCHAR(255) NOT NULL, SUBSCRIPTION_ID BIGINT NOT NULL, PRIMARY KEY (CONTENT_SELECTOR_ID));
CREATE TABLE PIPE_APP_NOTIFICATION (PIPE_ID BIGINT NOT NULL AUTO_INCREMENT, SUBSCRIPTION_ID BIGINT NOT NULL, PRIMARY KEY (PIPE_ID));
CREATE TABLE PIPE_NEWSLETTER (PIPE_ID BIGINT NOT NULL AUTO_INCREMENT, EMAIL VARCHAR(255), SUBSCRIPTION_ID BIGINT NOT NULL, PRIMARY KEY (PIPE_ID));
CREATE TABLE SUBSCRIPTION (SUBSCRIPTION_ID BIGINT NOT NULL AUTO_INCREMENT, SHARDING_ID INTEGER NOT NULL, TENANT VARCHAR(10) NOT NULL, UAS_ACCOUNT_ID VARCHAR(50), PRIMARY KEY (SUBSCRIPTION_ID));
ALTER TABLE APP_NOTIFICATION_DEVICES ADD CONSTRAINT FKktvjcapvlhitywegl694w1hvh FOREIGN KEY (PIPE_ID) REFERENCES PIPE_APP_NOTIFICATION (PIPE_ID);
CREATE INDEX CONTENT_SELECTOR_JOIN_IDX ON CONTENT_SELECTOR (SUBSCRIPTION_ID, CONTENT_SELECTOR);
ALTER TABLE CONTENT_SELECTOR ADD CONSTRAINT FK8vd8cq13x14pas2pp2kk203b9 FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES SUBSCRIPTION (SUBSCRIPTION_ID);
ALTER TABLE PIPE_APP_NOTIFICATION ADD CONSTRAINT FK75pr5tejv59ll3547xvpoas62 FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES SUBSCRIPTION (SUBSCRIPTION_ID);
ALTER TABLE PIPE_NEWSLETTER ADD CONSTRAINT FKjam8vv4r117f7ix8dlklfbmtr FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES SUBSCRIPTION (SUBSCRIPTION_ID);
ALTER TABLE SUBSCRIPTION ADD CONSTRAINT UAS_TENANT_KEY_IDX UNIQUE (UAS_ACCOUNT_ID, TENANT);
