package org.jboss.forge.scaffold.angularjs;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerClient {

    private Configuration config;

    public FreemarkerClient() {
        config = new Configuration();
        config.setClassForTemplateLoading(getClass(), "/scaffold");
        config.setObjectWrapper(new DefaultObjectWrapper());
    }

    public String processFTL(Map<String, Object> root, String inputPath) {
        try {
            Template templateFile = config.getTemplate(inputPath);
            Writer out = new StringWriter();
            templateFile.process(root, out);
            out.flush();
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
