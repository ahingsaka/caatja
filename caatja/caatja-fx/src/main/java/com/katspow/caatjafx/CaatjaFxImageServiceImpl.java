package com.katspow.caatjafx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

public class CaatjaFxImageServiceImpl implements CaatjaFxImageService {

	@Override
	public HashMap<String, String> getImages(
			HashMap<String, String> imagesToLoad) {
		HashMap<String, String> result = new HashMap<String, String>();

		// Put images on server
		for (Map.Entry<String, String> entry : imagesToLoad.entrySet()) {

			// System.out.println(entry.getKey());

			InputStream is = this.getClass().getResourceAsStream("images/" + entry.getValue());

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			try {

				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}

				buffer.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

			String base64Value = DatatypeConverter.printBase64Binary(buffer.toByteArray());

			result.put(entry.getKey(), base64Value);
		}

		return result;
	}

}
