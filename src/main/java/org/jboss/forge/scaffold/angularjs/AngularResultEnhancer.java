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
import org.jboss.forge.scaffoldx.metawidget.MetawidgetInspectorFacade;
import org.jboss.forge.shell.ShellPrompt;

public class AngularResultEnhancer {

    private ShellPrompt prompt;
    private MetawidgetInspectorFacade metawidgetInspectorFacade;
    private Project project;

    @Inject
    public AngularResultEnhancer(Project project, ShellPrompt prompt, MetawidgetInspectorFacade metawidgetInspectorFacade) {
        this.project = project;
        this.prompt = prompt;
        this.metawidgetInspectorFacade = metawidgetInspectorFacade;
    }

    public List<Map<String, String>> enhanceResults(JavaClass entity, List<Map<String, String>> inspectionResults) {
        for (Map<String, String> propertyAttributes : inspectionResults) {
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
        }
        return inspectionResults;
    }
    
    // TODO; Extract this method into it's own class, for unit testing.
    private List<String> getFieldsToDisplay(String rightHandSideType)
    {
       List<String> displayableProperties = new ArrayList<String>();
       JavaClass javaClass = getJavaClass(rightHandSideType);
       List<Map<String, String>> inspectionResults = metawidgetInspectorFacade.inspect(javaClass);
       for (Map<String, String> propertyAttributes : inspectionResults) {
           boolean isManyToOneRel = Boolean.parseBoolean(propertyAttributes.get("many-to-one"));
           boolean isOneToOneRel = Boolean.parseBoolean(propertyAttributes.get("one-to-one"));
           boolean isNToManyRel = Boolean.parseBoolean(propertyAttributes.get("n-to-many"));
           if (!isManyToOneRel && !isNToManyRel && !isOneToOneRel)
           {
              // Display only basic properties
              String hidden = propertyAttributes.get("hidden");
              String required = propertyAttributes.get("required");
              boolean isHidden = Boolean.parseBoolean(hidden);
              boolean isRequired = Boolean.parseBoolean(required);
              if (!isHidden)
              {
                 displayableProperties.add(propertyAttributes.get("name"));
              }
              else if (isRequired)
              {
                 // Do nothing if hidden, unless required
                 displayableProperties.add(propertyAttributes.get("name"));
              }
           }
       }
       return displayableProperties;
    }
    
    private String getSimpleName(String rightHandSideType)
    {
       return getJavaClass(rightHandSideType).getName();
    }
    
    private JavaClass getJavaClass(String qualifiedType)
    {
       JavaSourceFacet java = this.project.getFacet(JavaSourceFacet.class);
       try
       {
          JavaResource resource = java.getJavaResource(qualifiedType);
          JavaClass javaClass = (JavaClass) resource.getJavaSource();
          return javaClass;
       }
       catch (FileNotFoundException fileEx)
       {
          throw new RuntimeException(fileEx);
       }
    }

}
