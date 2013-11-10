<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
${message}
<form:form method="POST" commandName="file" action="/upload"
           enctype="multipart/form-data">

    <form:errors path="*" cssClass="errorblock" element="div" />

    Please select image 1 to upload : <input type="file" name="im1" />    <br/>
    Please select image 2 to upload : <input type="file" name="im2" />    <br/>
    Please select image 3 to upload : <input type="file" name="im3" />         <br/>
    <input type="submit" value="upload" />
		<span><form:errors path="file" cssClass="error" />
		</span>

</form:form>