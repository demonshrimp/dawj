package com.demonshrimp.dawj.web.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

public class EnumHttpMessageConverter extends AbstractHttpMessageConverter<Enum> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	@Override
	protected Enum readInternal(Class<? extends Enum> e, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
		String str = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
		return Enum.valueOf(e, str);
	}

	@Override
	protected boolean supports(Class<?> clz) {
		return Enum.class.isAssignableFrom(clz);
	}

	@Override
	protected void writeInternal(Enum e, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
		outputMessage.getHeaders().setContentType(MediaType.parseMediaType("application/json;Charset=UTF-8"));
		OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
		writer.write(e.toString());
		writer.close();
	}

	private Charset getContentTypeCharset(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			return contentType.getCharSet();
		} else {
			return DEFAULT_CHARSET;
		}
	}

}