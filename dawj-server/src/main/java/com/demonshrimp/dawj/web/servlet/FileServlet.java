package com.demonshrimp.dawj.web.servlet;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demonshrimp.dawj.model.prop.SystemConfigProperties;

/**
 * 
 * 显示文件的Servlet
 * 负责从本地文件中读取图片，
 * 
 */
public class FileServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	private static final String GIF = "image/gif;charset=GB2312";// 设定输出的类型

	private static final String JPG = "image/jpeg;charset=GB2312";

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public FileServlet() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String filePath = request.getRequestURI();
		filePath = filePath.substring(request.getContextPath().length()+8);
		SystemConfigProperties systemConfig = new SystemConfigProperties();
		String root = systemConfig.getUploadPath();
		if (root == null || root.equals("")) {
			root = request.getServletContext().getRealPath("/") + "/upload";
		}
		File rootFolder = new File(root);
		File file = new File(rootFolder, filePath);
		response.reset();

		OutputStream output = response.getOutputStream();// 得到输出流
		/*if (filePath.toLowerCase().endsWith(".jpg"))// 使用编码处理文件流的情况：
		{
			response.setContentType(JPG);// 设定输出的类型
			// 得到图片的真实路径

			// 得到图片的文件流
			InputStream imageIn = new FileInputStream(new File(imagePath));
			// 得到输入的编码器，将文件流进行jpg格式编码
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
			// 得到编码后的图片对象
			BufferedImage image = decoder.decodeAsBufferedImage();
			// 得到输出的编码器
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
			encoder.encode(image);// 对图片进行输出编码
			imageIn.close();// 关闭文件流
		}*/
			//response.setContentType(GIF);
			ServletContext context = getServletContext();// 得到背景对象
			InputStream imageIn = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(imageIn);// 输入缓冲流
			BufferedOutputStream bos = new BufferedOutputStream(output);// 输出缓冲流
			byte data[] = new byte[4096];// 缓冲字节数
			int size = 0;
			size = bis.read(data);
			while (size != -1) {
				bos.write(data, 0, size);
				size = bis.read(data);
			}
			bis.close();
			bos.flush();// 清空输出缓冲流
			bos.close();
		output.close();
	}
}