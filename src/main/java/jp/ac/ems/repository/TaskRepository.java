package jp.ac.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.UserBean;

/**
 * 課題用リポジトリ(taks repository).
 * @author tejc999999
 */
public interface TaskRepository extends JpaRepository<TaskBean, Long> {

}