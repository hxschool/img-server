package cn.tycoding.system.controller.router;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.tycoding.common.exception.GlobalException;
import cn.tycoding.common.utils.HttpContextUtil;
import cn.tycoding.common.utils.PasswordHelper;
import cn.tycoding.system.controller.UserController;
import cn.tycoding.system.entity.SysArticle;
import cn.tycoding.system.entity.SysUser;
import cn.tycoding.system.service.ArticleService;
import cn.tycoding.system.service.CategoryService;
import cn.tycoding.system.service.TagService;
import cn.tycoding.system.service.UserService;

/**
 * 博客后台路由控制层
 *
 * @author tycoding
 * @date 2019-09-10
 */
@Controller
public class SystemRouterController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @GetMapping("/system")
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/login")
    public String login() {
		HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
		Object object = request.getSession().getAttribute("_const_cas_assertion_");
		Assertion assertion = (Assertion)object;
		if (!StringUtils.isEmpty(assertion) && !StringUtils.isEmpty(assertion.getPrincipal())) {
			String username = assertion.getPrincipal().getName();
			String password = "123456";
			  SysUser sysUser = userService.findByName(username);
		        if (sysUser == null) {
		        	sysUser = new SysUser();
		        	sysUser.setUsername(username);
		        	sysUser.setPassword(password);
		        	PasswordHelper passwordHelper = new PasswordHelper();
		        	passwordHelper.encryptPassword(sysUser);
		        	userService.save(sysUser);
		        }
		        Subject subject = SecurityUtils.getSubject();    
		        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		        subject.login(token);
		}
        return "admin/page/login";
    }

    @GetMapping("/admin/page/layout")
    public String layout() {
        return "admin/page/layout";
    }

    @GetMapping("/admin/page/index")
    public String index() {
        return "admin/page/index";
    }

    @RequestMapping("/admin/page/article")
    public String article() {
        return "admin/page/article/index";
    }

    @RequestMapping("/admin/page/article/add")
    public String articleAdd() {
        return "admin/page/article/form";
    }

    @RequestMapping("/admin/page/article/edit/{id}")
    public String articleEdit(Model model, @PathVariable String id) {
        SysArticle article = articleService.getById(id);
        article.setTags(tagService.findByArticleId(article.getId()));
        model.addAttribute("p", article);
        return "admin/page/article/edit";
    }

    @RequestMapping("/admin/page/tag")
    public String tag() {
        return "admin/page/tag/index";
    }

    @RequestMapping("/admin/page/tag/edit")
    public String tagEdit() {
        return "admin/page/tag/form";
    }

    @RequestMapping("/admin/page/category")
    public String category() {
        return "admin/page/category/index";
    }

    @RequestMapping("/admin/page/category/edit")
    public String categoryEdit() {
        return "admin/page/category/form";
    }

    @RequestMapping("/admin/page/link")
    public String link() {
        return "admin/page/link/index";
    }

    @RequestMapping("/admin/page/link/edit")
    public String linkEdit() {
        return "admin/page/link/form";
    }

    @RequestMapping("/admin/page/comment")
    public String comment() {
        return "admin/page/comment/index";
    }

    @RequestMapping("/admin/page/log")
    public String log() {
        return "admin/page/log/index";
    }

    @RequestMapping("/admin/page/loginlog")
    public String loginlog() {
        return "admin/page/loginlog/index";
    }

    @RequestMapping("/admin/page/qiniu")
    public String qiniu() {
        return "admin/page/qiniu/index";
    }

    @RequestMapping("/admin/page/qiniu/add")
    public String qiniuAdd() {
        return "admin/page/qiniu/add";
    }

    @RequestMapping("/admin/page/user/profile")
    public String profile() {
        return "admin/page/user/profile";
    }

    @RequestMapping("/admin/page/user/avatar")
    public String avatar() {
        return "admin/page/user/avatar";
    }

    @RequestMapping("/admin/page/user/edit")
    public String userEdit() {
        return "admin/page/user/edit";
    }

    @RequestMapping("/admin/page/swagger")
    public String swagger() {
        return "swagger-ui.html";
    }
}
