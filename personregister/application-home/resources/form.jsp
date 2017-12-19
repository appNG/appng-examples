<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://appng.org/tags" prefix="appNG"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<style>
.error {
	color: red;
}
</style>

</head>
<body>

	<h1>An appNG Form</h1>
	<p>
		See <a href="https://appng.org/appng/docs/1.16.1/reference/html/developerguide.html#form-tags" target="_blank">the section about forms</a> in the
		developer guide for details.
	</p>
	<appNG:form>
		<appNG:formData mode="not_submitted">
			<form action="" method="post" enctype="multipart/form-data">

				<appNG:formElement rule="email" mandatory="true"
					mandatoryMessage="E-mail is mandatory!" errorClass="error"
					errorMessage="Not a valid e-mail!" errorElementId="emailError">
					<input type="text" name="email" />
					<div id="emailError"></div>
				</appNG:formElement>
				<input type="submit" />
			</form>
		</appNG:formData>
		<appNG:formConfirmation application="personregister"
			method="personFormProvider" mode="submitted">
			<appNG:param name="foo">bar</appNG:param>
			<appNG:param name="jin">fizz</appNG:param>
			<p>
				Thank you for your message!
				<p>
		</appNG:formConfirmation>
	</appNG:form>

</body>
</html>