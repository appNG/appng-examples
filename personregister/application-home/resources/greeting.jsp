<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://appng.org/tags" prefix="appNG"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head />
<body>

	<h1>An appNG taglet</h1>
	<p>
		See <a href="https://appng.org/appng/docs/1.16.1/reference/html/developerguide.html#appng-taglet" target="_blank">the section about taglets</a> in the
		developer guide for details.
	</p>
	<appNG:taglet application="personregister" method="personTaglet">
		<appNG:param name="greeting">Hello</appNG:param>
	</appNG:taglet>

</body>
</html>