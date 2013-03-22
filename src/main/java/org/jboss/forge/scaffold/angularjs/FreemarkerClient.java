package org.jboss.forge.scaffold.angularjs;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerClient {

    private Configuration config;

    public FreemarkerClient(File templateBaseDir) {
        config = new Configuration();
        List<TemplateLoader> loaders = new ArrayList<TemplateLoader>();
        if (templateBaseDir != null) {
            try {
                loaders.add(new FileTemplateLoader(templateBaseDir));
            } catch (IOException ioEx) {
                throw new RuntimeException(ioEx);
            }
        }
        loaders.add(new ClassTemplateLoader(getClass(), "/scaffold"));
        config.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0])));
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
