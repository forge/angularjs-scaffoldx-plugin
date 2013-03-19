'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('${projectTitle}', function() {

  describe('New ${entityName} View', function() {

    beforeEach(function() {
      browser().navigateTo('../../index.html');
    });

    it('should create a new ${entityName} and open the edit view for editing', function() {
      element('a:contains("${entityName}s")').click();
      expect(browser().location().path()).toEqual("/${entityName}s");

      element('#Create').click();
      expect(browser().location().path()).toEqual("/${entityName}s/new");
      
      <#list properties as property>
          <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
          
          <#elseif (property["n-to-many"]!) == "true">
          
          <#else>
            <#if property.type == "number"><#t/>
                        <#if property["minimum-value"]??>input('${entityName?uncap_first}.${property.name}').enter('${property["minimum-value"]}');<#t/>
                        <#elseif property["maximum-value"]??>input('${entityName?uncap_first}.${property.name}').enter('${property["maximum-value"]}');</#if><#t/>
                    <#elseif (property["datetime-type"]!"") == "date">input('${entityName?uncap_first}.${property.name}').enter('2000-01-01');<#t/>
                    <#elseif (property["datetime-type"]!"") == "time">input('${entityName?uncap_first}.${property.name}').enter('12:00');<#t/>
                    <#elseif (property["datetime-type"]!"") == "both">input('${entityName?uncap_first}.${property.name}').enter('2000-01-01 12:00');<#t/>
                    <#else>input('${entityName?uncap_first}.${property.name}').enter('Test');</#if><#t/>
          </#if>
      </#list>
      
      element('#save${entityName}').click();
      
      expect(browser().location().path()).toContain("/${entityName}s/edit/");
      <#list properties as property>
          <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
          
          <#elseif (property["n-to-many"]!) == "true">
          
          <#else>
            <#if property.type == "number"><#t/>
                        <#if property["minimum-value"]??>expect(input('${entityName?uncap_first}.${property.name}').val()).toEqual('${property["minimum-value"]}');<#t/>
                        <#elseif property["maximum-value"]??>expect(input('${entityName?uncap_first}.${property.name}').val()).toEqual('${property["maximum-value"]}');</#if><#t/>
                    <#elseif (property["datetime-type"]!"") == "date">expect(input('${entityName?uncap_first}.${property.name}').val()).toEqual('2000-01-01');<#t/>
                    <#elseif (property["datetime-type"]!"") == "time">expect(input('${entityName?uncap_first}.${property.name}').val()).toEqual('12:00');<#t/>
                    <#elseif (property["datetime-type"]!"") == "both">expect(input('${entityName?uncap_first}.${property.name}').val()).toEqual('2000-01-01 12:00');<#t/>
                    <#else>expect(input('${entityName?uncap_first}.${property.name}').val()).toEqual('Test');</#if><#t/>
          </#if>
      </#list>
      
    });
  });
});
