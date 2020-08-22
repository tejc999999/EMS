package jp.ac.ems.controller;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 共通トップContollerクラス（common top Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/top")
public class TopController {

    /**
     * トップ画面情報表示(top page info view).
     * @param model トップ画面保存用モデル(model to save top page)
     * @return トップ画面ページビュー(top page view)
     */
    @GetMapping
    String top(Model model) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ROLE_STUDENT"));
        if(authorized) {
        	// 学生の場合のみ、学生用TOP画面に移動
        	
        	return "redirect:student/top";
        } else {
        	// 管理者、先生の場合は共通TOP画面に移動
        	
        	return "top";
        }
    }
}
