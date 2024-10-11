create table permissions
(
    id               bigint auto_increment
        primary key,
    name             varchar(45) not null,
    permission_flags int         not null
);

create table serial_config
(
    id           bigint auto_increment
        primary key,
    baud_rate    int          null,
    databits     int          null,
    flow_control varchar(255) null,
    parity       varchar(255) null,
    serial_port  varchar(255) null
);

create table sites
(
    id          bigint auto_increment
        primary key,
    description varchar(255) null,
    dt_created  datetime(6)  null,
    name        varchar(255) null
);

create table users
(
    id         bigint auto_increment
        primary key,
    email      varchar(45)  not null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    user_name  varchar(45)  not null,
    site_id    bigint       null,
    constraint UK_6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UK_k8d0f2n7n88w1a16yhua64onx
        unique (user_name),
    constraint FKnt2bgjpwcylpcl8exup1hvewd
        foreign key (site_id) references sites (id)
);

create table mqtt_config
(
    id             bigint auto_increment
        primary key,
    client_id      decimal(38, 2) null,
    url            varchar(255)   null,
    site_entity_id bigint         null,
    user_entity_id bigint         null,
    constraint UK_rny72p9ngq5up2sb805h0pauk
        unique (site_entity_id),
    constraint FK232ogdfh7ahgow6jtf70q6bd0
        foreign key (site_entity_id) references sites (id),
    constraint FKrw9c3e76cy0syph9dt5yqwakb
        foreign key (user_entity_id) references users (id)
);

create table devices
(
    id               bigint auto_increment
        primary key,
    firmware_version varchar(255) null,
    hardware_version varchar(255) null,
    mac_address      varchar(255) null,
    manufacturer_id  varchar(255) null,
    model_id         varchar(255) null,
    mqtt_config_id   bigint       null,
    serial_config_id bigint       null,
    site_id          bigint       null,
    constraint UK_8l9oeyg322b76bihnbv3wyswq
        unique (mqtt_config_id),
    constraint UK_bfdu66xgk53rkn5023ce70rvw
        unique (mac_address),
    constraint UK_dda8la44k6729tu8ecc7mu8ip
        unique (serial_config_id),
    constraint FK4g3cv6w4n862f1rqs8wuncsxu
        foreign key (mqtt_config_id) references mqtt_config (id),
    constraint FKit1auw1j4l9k1l3mn656ee5li
        foreign key (site_id) references sites (id),
    constraint FKl207g33ol8w6hegeglck00mot
        foreign key (serial_config_id) references serial_config (id)
);

create table actuators
(
    id          bigint auto_increment
        primary key,
    description varchar(255) null,
    mqtt_topic  varchar(255) null,
    name        varchar(255) null,
    device_id   bigint       null,
    constraint FKii9xrjnvlde8bq8dowwfbgwdk
        foreign key (device_id) references devices (id)
);

create table mqtt_topics
(
    id                    bigint auto_increment
        primary key,
    path                  varchar(255) null,
    mqtt_config_entity_id bigint       null,
    constraint FKls071co26fxkeor8jvhq5c7fh
        foreign key (mqtt_config_entity_id) references mqtt_config (id)
);

create table mqtt_config_mqtt_topic_entities
(
    mqtt_config_entity_id  bigint not null,
    mqtt_topic_entities_id bigint not null,
    constraint FK1orfuyqg5s1u2hy92te7lmoa5
        foreign key (mqtt_topic_entities_id) references mqtt_topics (id),
    constraint FKh2206hvyphw27dwwvamyfqqh4
        foreign key (mqtt_config_entity_id) references mqtt_config (id)
);

create table sensors
(
    id                bigint auto_increment
        primary key,
    description       varchar(255) null,
    mqtt_topic        varchar(255) null,
    name              varchar(255) null,
    sensor_type_value int          not null,
    device_id         bigint       null,
    constraint UK_pwxr50puxe55nd0249ybf21vs
        unique (name),
    constraint FK834yqd4p6g9s2b6ufnba8bxrs
        foreign key (device_id) references devices (id)
);

create table domiot_parameters
(
    id             bigint auto_increment
        primary key,
    name           varchar(255) null,
    parameter_type varchar(255) null,
    readonly       bit          null,
    value          varchar(255) null,
    actuator_id    bigint       null,
    device_id      bigint       null,
    sensor_id      bigint       null,
    constraint FK2j5c9sea7vxblcltq0l8pauov
        foreign key (actuator_id) references actuators (id),
    constraint FKdkhhdo9jgjnj6202fn7t95hwv
        foreign key (device_id) references devices (id),
    constraint FKjv35ntm6uo1a8ac4n88afvtev
        foreign key (sensor_id) references sensors (id)
);

create table sensor_values
(
    id         bigint auto_increment
        primary key,
    time_stamp datetime(6) null,
    value      double      not null,
    sensor_id  bigint      not null,
    constraint FKj6tr7g3du8m1f2tc7erwhitkv
        foreign key (sensor_id) references sensors (id)
);

create table users_permissions
(
    user_entity_id         bigint not null,
    permission_entities_id bigint not null,
    constraint FKbb38kporbqh6rftaji781jbmu
        foreign key (permission_entities_id) references permissions (id),
    constraint FKjqfoh4xru77fvbirr1wn3s2jw
        foreign key (user_entity_id) references users (id)
);

