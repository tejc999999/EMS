package jp.spring.boot.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.ems.bean.TaskBean;

/**
 * 課題用リポジトリ(taks repository).
 * @author tejc999999
 */
public interface TaskRepository extends JpaRepository<TaskBean, Integer> {

}
