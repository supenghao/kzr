DROP TABLE IF EXISTS `T_S_Token`;
create table T_S_Token (
        id  bigint UNSIGNED AUTO_INCREMENT,
        token varchar(36),
        userid bigint UNSIGNED unique,
        primary key (id)
 );
 
 alter table T_S_Token 
        add index FK4D3C9D9838C7CAC (userid), 
        add constraint FK4D3C9D9838C7CAC 
        foreign key (userid) 
        references T_S_User (id);
 
 DROP TABLE IF EXISTS `T_S_MemberInfo`; 
 create table T_S_MemberInfo(
        id bigint UNSIGNED AUTO_INCREMENT,
        userid bigint UNSIGNED unique,
        cityCode varchar(255),
        districtCode varchar(255),
        headPic varchar(255),
        nickName varchar(255),
        provinceCode varchar(255),
        sex varchar(2),
        terminalCode varchar(255),
        userEmail varchar(255),
        userMobile varchar(255),
        usersTreet varchar(255),
        primary key (id)
    );
    
 alter table T_S_MemberInfo 
        add index FK4D3C9D9838C7CAD (userid), 
        add constraint FK4D3C9D9838C7CAD 
        foreign key (userid) 
        references T_S_User (id);
        
DROP TABLE IF EXISTS `t_s_message`; 
create table t_s_message (
        messagecode bigint(10) UNSIGNED AUTO_INCREMENT not null,
        create_user bigint,
        messageDate datetime default now() not null,
        messageDes varchar(255) not null,
        messageDetail varchar(255) not null,
        messageName varchar(100) not null,
        messageStatus varchar(1) default '1',
        primary key (messagecode)
);

 alter table t_s_message 
        add UNIQUE index FK4D3C9D9838C7CAE (messagecode);
        
 DROP TABLE IF EXISTS `t_s_banks`; 
create table t_s_banks (
        id bigint(10) UNSIGNED AUTO_INCREMENT not null,
        bankName varchar(255) not null,
        bankCode bigint(10) UNSIGNED not null,
        primary key (id)
);

 alter table t_s_banks 
        add UNIQUE index FK4D3C9D9838C7CAF (id),
        add index FK4D3C9D983827CAF (bankCode);
        
DROP TABLE IF EXISTS `t_s_creditcar`; 
create table t_s_creditcar (
        id bigint(20) UNSIGNED AUTO_INCREMENT not null,
        userId bigint UNSIGNED not null,
        cardNo varchar(30) not null,
        bankCode varchar(20) not null,
        billDate varchar(20) not null,
		mail_addr varchar(100) not null,
		mail_passwd varchar(20) not null,
        foreign key (userId)  references T_S_User (id),
        primary key (id)
);

 alter table t_s_creditcar 
        add UNIQUE index FK4D3C9D9838C7CB0 (id),
        add index FK4D3C9D9828C7CB0 (userId);

 
        
 