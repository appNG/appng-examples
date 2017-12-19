<%@page pageEncoding="utf-8" contentType="application/json; charset=utf-8"%>
<%@taglib uri="http://appng.org/tags" prefix="appNG"%>
<appNG:search parts="true" format="json" highlight="span">
        <appNG:param name="queryParam">term</appNG:param>
        <appNG:param name="pageSize">10</appNG:param>
        <appNG:param name="pageParam">p</appNG:param>
        <appNG:param name="pretty">true</appNG:param>
        
        <appNG:searchPart
            application="global"
            language="en"
            title="Search Results"
            fields="title,contents" analyzerClass="org.apache.lucene.analysis.en.EnglishAnalyzer"/>

        <appNG:searchPart
            application="personregister"
            method="personSearchProvider"
            language="en"
            title="Person Search Results"
            fields="title" analyzerClass="org.apache.lucene.analysis.en.EnglishAnalyzer"/>

 </appNG:search>
