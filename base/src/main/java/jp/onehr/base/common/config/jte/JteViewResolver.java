package jp.onehr.base.common.config.jte;

import gg.jte.TemplateEngine;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JteViewResolver extends AbstractTemplateViewResolver {
    
    private final TemplateEngine templateEngine;

    public JteViewResolver(TemplateEngine templateEngine) {
        this.setViewClass(this.requiredViewClass());
        this.setSuffix(".jte");
        this.templateEngine = templateEngine;
    }

    @Override
    public AbstractUrlBasedView instantiateView() {
        return new JteView(templateEngine);
    }

}
