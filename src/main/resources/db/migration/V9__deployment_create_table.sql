CREATE TABLE `server_viewer`.`deployment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `application_id` INT NOT NULL,
  `server_id` INT NOT NULL,
  `middleware_id` INT NULL,
  `runs_in_docker` TINYINT(1) NOT NULL DEFAULT 0,
  `application_url` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));