package com.junerking.particle.utils;

import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.junerking.particle.CCParticleSystem;
import com.junerking.particle.CCQuadParticleSystem;
import com.junerking.utils.PlistSAXHandler;

public class ParticleManager {
	public static CCParticleSystem create(String file_name, TextureAtlas atlas) {

		if (file_name == null || file_name.equals(""))
			return null;

		try {

			long start = System.currentTimeMillis(), end;

			FileInputStream uri = new FileInputStream(Gdx.files.internal(file_name).file());
			PlistSAXHandler plistHandler = new PlistSAXHandler();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(uri, plistHandler);

			end = System.currentTimeMillis();
			System.out.println("==== " + (end - start));

			CCParticleSystem result = new CCQuadParticleSystem();
			result.loadParticleFile(plistHandler.getMapResult(), atlas);
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
