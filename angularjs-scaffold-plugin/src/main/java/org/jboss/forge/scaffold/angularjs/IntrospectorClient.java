package org.jboss.forge.scaffold.angularjs;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.scaffold.angularjs.metawidget.inspector.ForgeInspector;
import org.jboss.forge.scaffold.angularjs.metawidget.inspector.propertystyle.ForgePropertyStyle;
import org.jboss.forge.scaffold.angularjs.metawidget.inspector.propertystyle.ForgePropertyStyleConfig;
import org.jboss.forge.shell.ShellPrompt;
import org.metawidget.inspector.beanvalidation.BeanValidationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jpa.JpaInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

public class IntrospectorClient {

    private Project project;
    
    private ShellPrompt prompt;
    
    @Inject
    public IntrospectorClient(Project project, ShellPrompt prompt) {
        this.project = project;
        this.prompt = prompt;
    }

    public List<Map<String, String>> inspect(JavaClass entity) {
        Element inspectionResult = inspectEntity(entity);
        Element inspectedEntity = XmlUtils.getFirstChildElement(inspectionResult);
        System.out.println(XmlUtils.nodeToString(inspectedEntity, true));

        Element inspectedProperty = XmlUtils.getFirstChildElement(inspectedEntity);
        List<Map<String, String>> viewPropertyAttributes = new ArrayList<Map<String,String>>();
        while (inspectedProperty != null) {
            System.out.println(XmlUtils.nodeToString(inspectedProperty, true));
            Map<String, String> propertyAttributes = XmlUtils.getAttributesAsMap(inspectedProperty);
            
            // Canonicalize all numerical types in Java to "number" for HTML5 form input type support
            String propertyType = propertyAttributes.get("type");
            if (propertyType.equals(short.class.getName()) || propertyType.equals(int.class.getName())
                    || propertyType.equals(long.class.getName()) || propertyType.equals(float.class.getName())
                    || propertyType.equals(double.class.getName()) || propertyType.equals(Short.class.getName())
                    || propertyType.equals(Integer.class.getName()) || propertyType.equals(Long.class.getName())
                    || propertyType.equals(Float.class.getName()) || propertyType.equals(Double.class.getName())) {
                propertyAttributes.put("type", "number");
            }

            // Extract simple type name of the relationship types
            boolean isManyToOneRel = Boolean.parseBoolean(propertyAttributes.get("many-to-one"));
            boolean isOneToOneRel = Boolean.parseBoolean(propertyAttributes.get("one-to-one"));
            boolean isNToManyRel = Boolean.parseBoolean(propertyAttributes.get("n-to-many"));
            if (isManyToOneRel || isNToManyRel || isOneToOneRel) {
                String rightHandSideType;
                if (isOneToOneRel || isManyToOneRel) {
                    rightHandSideType = propertyAttributes.get("type");
                } else {
                    rightHandSideType = propertyAttributes.get("parameterized-type");
                }
                String rightHandSideSimpleName = getSimpleName(rightHandSideType);
                propertyAttributes.put("simpleType", rightHandSideSimpleName);
                List<String> fieldsToDisplay = getFieldsToDisplay(rightHandSideType);
                String defaultField = fieldsToDisplay.size() > 0 ? fieldsToDisplay.get(0) : null;
                propertyAttributes.put("optionLabel", prompt.promptChoiceTyped("Which property of " + rightHandSideSimpleName
                        + " do you want to display in the " + entity.getName() + " views ?", fieldsToDisplay, defaultField));
            }

            // Add the property attributes into a list, made accessible as a sequence to the FTL
            viewPropertyAttributes.add(propertyAttributes);
            inspectedProperty = XmlUtils.getNextSiblingElement(inspectedProperty);
        }
        return viewPropertyAttributes;
    }

    // TODO; Extract this method into it's own class, for unit testing.
    private List<String> getFieldsToDisplay(String manyToOneType) {
        List<String> displayableProperties = new ArrayList<String>();
        JavaClass javaClass = getJavaClass(manyToOneType);
        Element inspectionResult = inspectEntity(javaClass);
        Element inspectedEntity = XmlUtils.getFirstChildElement(inspectionResult);
        Element inspectedProperty = XmlUtils.getFirstChildElement(inspectedEntity);
        while (inspectedProperty != null) {
            Map<String, String> propertyAttributes = XmlUtils.getAttributesAsMap(inspectedProperty);
            boolean isManyToOneRel = Boolean.parseBoolean(propertyAttributes.get("many-to-one"));
            boolean isOneToOneRel = Boolean.parseBoolean(propertyAttributes.get("one-to-one"));
            boolean isNToManyRel = Boolean.parseBoolean(propertyAttributes.get("n-to-many"));
            if (!isManyToOneRel && !isNToManyRel && !isOneToOneRel) {
                // Display only basic properties
                String hidden = propertyAttributes.get("hidden");
                String required = propertyAttributes.get("required");
                boolean isHidden = Boolean.parseBoolean(hidden);
                boolean isRequired = Boolean.parseBoolean(required);
                if (!isHidden) {
                    displayableProperties.add(propertyAttributes.get("name"));
                } else if (isRequired) {
                    // Do nothing if hidden, unless required
                    displayableProperties.add(propertyAttributes.get("name"));
                }
            }
            inspectedProperty = XmlUtils.getNextSiblingElement(inspectedProperty);
        }
        return displayableProperties;
    }

    private Element inspectEntity(JavaClass entity) {
        ForgePropertyStyleConfig forgePropertyStyleConfig = new ForgePropertyStyleConfig();
        forgePropertyStyleConfig.setProject(this.project);
        BaseObjectInspectorConfig baseObjectInspectorConfig = new BaseObjectInspectorConfig();
        baseObjectInspectorConfig.setPropertyStyle(new ForgePropertyStyle(forgePropertyStyleConfig));

        PropertyTypeInspector propertyTypeInspector = new PropertyTypeInspector(baseObjectInspectorConfig);

        ForgeInspector forgeInspector = new ForgeInspector(baseObjectInspectorConfig);

        JpaInspectorConfig jpaInspectorConfig = new JpaInspectorConfig();
        jpaInspectorConfig.setHideIds(true);
        jpaInspectorConfig.setHideVersions(true);
        jpaInspectorConfig.setHideTransients(true);
        jpaInspectorConfig.setPropertyStyle(new ForgePropertyStyle(forgePropertyStyleConfig));
        JpaInspector jpaInspector = new JpaInspector(jpaInspectorConfig);

        BeanValidationInspector beanValidationInspector = new BeanValidationInspector(baseObjectInspectorConfig);

        CompositeInspectorConfig compositeInspectorConfig = new CompositeInspectorConfig();
        compositeInspectorConfig.setInspectors(propertyTypeInspector, forgeInspector, jpaInspector, beanValidationInspector);
        CompositeInspector compositeInspector = new CompositeInspector(compositeInspectorConfig);

        Element inspectionResult = compositeInspector.inspectAsDom(null, entity.getQualifiedName(), (String[]) null);
        return inspectionResult;
    }

    private String getSimpleName(String manyToOneType) {
        return getJavaClass(manyToOneType).getName();
    }

    private JavaClass getJavaClass(String qualifiedType) {
        JavaSourceFacet java = this.project.getFacet(JavaSourceFacet.class);
        try {
            JavaResource resource = java.getJavaResource(qualifiedType);
            JavaClass javaClass = (JavaClass) resource.getJavaSource();
            return javaClass;
        } catch (FileNotFoundException fileEx) {
            throw new RuntimeException(fileEx);
        }
    }
    
}
