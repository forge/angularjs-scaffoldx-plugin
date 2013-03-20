package org.jboss.forge.scaffold.angularjs.metawidget;

import static org.jboss.forge.scaffold.matchers.InspectionResultMatcher.hasItemWithEntry;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.TemporalType;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.scaffold.angularjs.AbstractHtml5ScaffoldTest;
import org.jboss.forge.scaffold.angularjs.IntrospectorClient;
import org.junit.Test;

public class IntrospectorClientTest extends AbstractHtml5ScaffoldTest {

    @Inject
    private IntrospectorClient introspectorClient;

    @Test
    public void testInspectBasicField() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);
        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
    }

    @Test
    public void testInspectBasicFieldNotNull() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateNotNullConstraint(fieldName);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("required", "true"));
    }

    @Test
    public void testInspectBasicFieldMinSize() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateSizeConstraint(fieldName, "5", null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("minimum-length", "5"));
    }

    @Test
    public void testInspectBasicFieldMaxSize() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateSizeConstraint(fieldName, null, "5");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("maximum-length", "5"));
    }

    @Test
    public void testInspectBasicFieldMinAndMaxSize() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateSizeConstraint(fieldName, "5", "50");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("minimum-length", "5"));
        assertThat(inspectionResult, hasItemWithEntry("maximum-length", "50"));
    }

    @Test
    public void testInspectBooleanField() throws Exception {
        String entityName = "Customer";
        String fieldName = "optForMail";
        generateSimpleEntity(entityName);
        generateBooleanField(fieldName);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "boolean"));
    }
    
    @Test
    public void testInspectDateField() throws Exception {
        String entityName = "Customer";
        String fieldName = "dateOfBirth";
        generateSimpleEntity(entityName);
        generateTemporalField(fieldName, TemporalType.DATE);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("datetime-type", "date"));
    }

    @Test
    public void testInspectTimeField() throws Exception {
        String entityName = "Customer";
        String fieldName = "dailyAlertTime";
        generateSimpleEntity(entityName);
        generateTemporalField(fieldName, TemporalType.TIME);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("datetime-type", "time"));
    }

    @Test
    public void testInspectDateTimeField() throws Exception {
        String entityName = "Customer";
        String fieldName = "lastUpdated";
        generateSimpleEntity(entityName);
        generateTemporalField(fieldName, TemporalType.TIMESTAMP);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("datetime-type", "both"));
    }

    @Test
    public void testInspectIntNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectLongNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "long");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectFloatNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, java.lang.Float.class);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectDoubleNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, java.lang.Double.class);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectNumberFieldMinValue() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");
        generateMinConstraint(fieldName, "0");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
        assertThat(inspectionResult, hasItemWithEntry("minimum-value", "0"));
    }

    @Test
    public void testInspectNumberFieldMaxValue() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");
        generateMaxConstraint(fieldName, "100");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
        assertThat(inspectionResult, hasItemWithEntry("maximum-value", "100"));
    }

    @Test
    public void testInspectNumberFieldMinAndMaxValue() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");
        generateMinConstraint(fieldName, "0");
        generateMaxConstraint(fieldName, "100");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
        assertThat(inspectionResult, hasItemWithEntry("minimum-value", "0"));
        assertThat(inspectionResult, hasItemWithEntry("maximum-value", "100"));
    }
    
    @Test
    public void testInspectOneToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "address";
        String relatedEntityName = "CustomerAddress";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToOneField(fieldName, relatedEntityType, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("one-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }
    
    @Test
    public void testInspectManyToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "category";
        String relatedEntityName = "CustomCategory";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToOneField(fieldName, relatedEntityType, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("many-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }

    @Test
    public void testInspectOneToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "orders";
        String relatedEntityName = "StoreOrder";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToManyField(fieldName, relatedEntityType, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
    }
    
    @Test
    public void testInspectManyToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "visitedStores";
        String relatedEntityName = "Store";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToManyField(fieldName, relatedEntityType, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
    }
    
    @Test
    public void testInspectBidiOneToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "address";
        String relatedEntityName = "CustomerAddress";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToOneField(fieldName, relatedEntityType, "customer");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("one-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }
    
    @Test
    public void testInspectBidiManyToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "category";
        String relatedEntityName = "CustomCategory";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToOneField(fieldName, relatedEntityType, "customer");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("many-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }

    @Test
    public void testInspectBidiOneToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "orders";
        String relatedEntityName = "StoreOrder";
        String inverseFieldName = "customer";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToManyField(fieldName, relatedEntityType, inverseFieldName);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
        assertThat(inspectionResult, hasItemWithEntry("inverse-relationship", inverseFieldName));
    }
    
    @Test
    public void testInspectBidiManyToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "visitedStores";
        String relatedEntityName = "Store";
        String inverseFieldName = "customer";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToManyField(fieldName, relatedEntityType, inverseFieldName);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = introspectorClient.inspect(klass);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
        // TODO: The inverse-relatioship is not available for ManyToMany bidi relations. Investigate. 
        //assertThat(inspectionResult, hasItemWithEntry("inverse-relationship", inverseFieldName));
    }

    private JavaClass getJavaClassFor(String entityName) throws FileNotFoundException {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        JavaClass klass = (JavaClass) java.getJavaResource(java.getBasePackage() + ".model." + entityName).getJavaSource();
        return klass;
    }
    
    private String getJavaClassNameFor(String entityName) throws FileNotFoundException {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        return java.getBasePackage() + ".model." + entityName;
    }

    private void generateSimpleEntity(String entityName) throws Exception {
        queueInputLines("");
        getShell().execute("entity --named " + entityName);
    }

    private void generateStringField(String fieldName) throws Exception {
        getShell().execute("field string --named " + fieldName);
    }
    
    private void generateBooleanField(String fieldName) throws Exception {
        getShell().execute("field boolean --named " + fieldName);
    }

    private void generateTemporalField(String fieldName, TemporalType type) throws Exception {
        getShell().execute("field temporal --type " + type + " --named " + fieldName);
    }

    private void generateNumericField(String fieldName, String type) throws Exception {
        if (type.equals("int") || type.equals("long")) {
            getShell().execute("field " + type + " --named " + fieldName);
        } else {
            throw new RuntimeException("Incorrect field type provided as input");
        }
    }

    private void generateNumericField(String fieldName, Class<? extends Number> klass) throws Exception {
        getShell().execute("field number --named " + fieldName + " --type " + klass.getName().toString());
    }
    
    private void generateOneToOneField(String fieldName, String type, String inverseFieldName) throws Exception {
        String command = "field oneToOne --named " + fieldName + " --fieldType " + type;
        if (inverseFieldName != null && !inverseFieldName.equals("")) {
            command += " --inverseFieldName " + inverseFieldName;
        }
        getShell().execute(command);
    }

    private void generateManyToOneField(String fieldName, String type, String inverseFieldName) throws Exception {
        String command = "field manyToOne --named " + fieldName + " --fieldType " + type;
        if (inverseFieldName != null && !inverseFieldName.equals("")) {
            command += " --inverseFieldName " + inverseFieldName;
        }
        getShell().execute(command);
    }

    private void generateOneToManyField(String fieldName, String type, String inverseFieldName) throws Exception {
        String command = "field oneToMany --named " + fieldName + " --fieldType " + type;
        if (inverseFieldName != null && !inverseFieldName.equals("")) {
            command += " --inverseFieldName " + inverseFieldName;
        }
        getShell().execute(command);
    }

    private void generateManyToManyField(String fieldName, String type, String inverseFieldName) throws Exception {
        String command = "field manyToMany --named " + fieldName + " --fieldType " + type;
        if (inverseFieldName != null && !inverseFieldName.equals("")) {
            command += " --inverseFieldName " + inverseFieldName;
        }
        getShell().execute(command);
    }

    private void generateNotNullConstraint(String fieldName) throws Exception {
        getShell().execute("constraint NotNull --onProperty " + fieldName);
    }

    private void generateSizeConstraint(String fieldName, String minSize, String maxSize) throws Exception {
        String command = "constraint Size --onProperty " + fieldName;
        if (minSize != null && !minSize.equals("")) {
            command += " --min " + minSize;
        }
        if (maxSize != null && !maxSize.equals("")) {
            command += " --max " + maxSize;
        }
        getShell().execute(command);
    }

    private void generateMinConstraint(String fieldName, String minValue) throws Exception {
        String command = "constraint Min --onProperty " + fieldName + " --min " + minValue;
        getShell().execute(command);
    }

    private void generateMaxConstraint(String fieldName, String maxValue) throws Exception {
        String command = "constraint Max --onProperty " + fieldName + " --max " + maxValue;
        getShell().execute(command);
    }
}
