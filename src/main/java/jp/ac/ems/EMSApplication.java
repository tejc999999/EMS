package jp.ac.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot Applicationクラス(SpringBoot Application class).
 * @author tejc999999
 */
@SpringBootApplication
public class EMSApplication {

    /**
     * メイン処理(main process).
     * @param args 実行時引数(run time arguments)
     */
    public static void main(String[] args) {
        SpringApplication.run(EMSApplication.class, args);
    }

}
