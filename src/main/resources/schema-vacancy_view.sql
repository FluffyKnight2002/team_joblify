CREATE
VIEW `vacancy_view` AS
SELECT
    `vi`.`id` AS `id`,
    `vi`.`close_date` AS `close_date`,
    `vi`.`description` AS `description`,
    `vi`.`hired_post` AS `hired_post`,
    `vi`.`job_type` AS `job_type`,
    `vi`.`lvl` AS `level`,
    `vi`.`note` AS `note`,
    `vi`.`on_site_or_remote` AS `on_site_or_remote`,
    `vi`.`open_date` AS `open_date`,
    `vi`.`post` AS `post`,
    `vi`.`preferences` AS `preferences`,
    `vi`.`requirements` AS `requirements`,
    `vi`.`responsibilities` AS `responsibilities`,
    `vi`.`salary` AS `salary`,
    `vi`.`updated_time` AS `updated_time`,
    `vi`.`working_hours` AS `working_hours`,
    `vi`.`working_days` AS `working_days`,
    COUNT(`c`.`id`) AS `applicants`,
    `v`.`id` AS `vacancy_id`,
    `v`.`created_date` AS `created_date`,
    `v`.`status` AS `status`,
    `p`.`name` AS `position`,
    `d`.`name` AS `department`,
    `a`.`name` AS `address`,
    `uc`.`name` AS `created_username`,
    `uu`.`name` AS `updated_username`
FROM
    (((((((`vacancy_info` `vi`
        JOIN `vacancy` `v` ON ((`v`.`id` = `vi`.`vacancy_id`)))
        JOIN `position` `p` ON ((`p`.`id` = `v`.`position_id`)))
        JOIN `department` `d` ON ((`d`.`id` = `v`.`department_id`)))
        JOIN `address` `a` ON ((`a`.`id` = `v`.`address_id`)))
        JOIN `user` `uc` ON ((`uc`.`id` = `v`.`created_user_id`)))
        JOIN `user` `uu` ON ((`uu`.`id` = `vi`.`updated_user_id`)))
        LEFT JOIN `candidate` `c` ON ((`c`.`vacancy_info_id` = `vi`.`id`)))
GROUP BY `vi`.`id` , `vi`.`close_date` , `vi`.`description` , `vi`.`hired_post` , `vi`.`job_type` , `vi`.`lvl` , `vi`.`note` , `vi`.`on_site_or_remote` , `vi`.`open_date` , `vi`.`post` , `vi`.`preferences` , `vi`.`requirements` , `vi`.`responsibilities` , `vi`.`salary` , `vi`.`updated_time` , `vi`.`working_hours` , `vi`.`working_days` , `v`.`id` , `v`.`created_date` , `v`.`status` , `p`.`name` , `d`.`name` , `a`.`name` , `uc`.`name` , `uu`.`name`