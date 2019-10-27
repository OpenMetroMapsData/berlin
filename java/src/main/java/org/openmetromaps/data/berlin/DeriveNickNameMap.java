// Copyright 2018 Sebastian Kuerten
//
// This file is part of OpenMetroMaps.
//
// OpenMetroMaps is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// OpenMetroMaps is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with OpenMetroMaps. If not, see <http://www.gnu.org/licenses/>.

package org.openmetromaps.data.berlin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.openmetromaps.maps.Edges;
import org.openmetromaps.maps.Interval;
import org.openmetromaps.maps.MapModel;
import org.openmetromaps.maps.MapView;
import org.openmetromaps.maps.model.Station;
import org.openmetromaps.maps.xml.DesktopXmlModelReader;
import org.openmetromaps.maps.xml.XmlModel;
import org.openmetromaps.maps.xml.XmlModelConverter;
import org.openmetromaps.maps.xml.XmlModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

import de.topobyte.xml.domabstraction.iface.ParsingException;

public class DeriveNickNameMap
{

	final static Logger logger = LoggerFactory
			.getLogger(DeriveNickNameMap.class);

	static class NickNameDef
	{

		private String name;
		private String nickName;

		public NickNameDef(String name, String nickName)
		{
			this.name = name;
			this.nickName = nickName;
		}

	}

	private static Map<String, Station> nameToStation = new HashMap<>();

	public static void main(String[] args) throws ParsingException, IOException,
			ParserConfigurationException, TransformerException
	{
		Path repo = Util.repoDir();
		Path fileInput = repo.resolve("schematic.omm");
		Path fileDef = repo.resolve("nicknames.txt");
		Path fileOutput = repo.resolve("nicknames.omm");

		// Read nicknames from text file
		List<NickNameDef> defs = new ArrayList<>();
		BufferedReader reader = Files.newBufferedReader(fileDef,
				StandardCharsets.UTF_8);
		Splitter splitter = Splitter.on(":").trimResults();
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			if (line.trim().isEmpty()) {
				continue;
			}
			List<String> parts = splitter.splitToList(line);
			String name = parts.get(0);
			String nickName = parts.get(1);
			defs.add(new NickNameDef(name, nickName));
		}
		reader.close();

		// Read original map
		InputStream input = Files.newInputStream(fileInput);
		XmlModel xmlModel = DesktopXmlModelReader.read(input);
		input.close();

		XmlModelConverter converter = new XmlModelConverter();
		MapModel model = converter.convert(xmlModel);

		// Rename stations
		List<Station> stations = model.getData().stations;
		for (Station station : stations) {
			nameToStation.put(station.getName(), station);
		}

		Map<String, String> replacements = new HashMap<>();
		for (NickNameDef def : defs) {
			replacements.put(def.name, def.nickName);
		}

		for (NickNameDef def : defs) {
			rename(def.name, def.nickName);
		}

		for (MapView view : model.getViews()) {
			List<Edges> allEdges = view.getEdges();
			for (Edges edges : allEdges) {
				for (Interval interval : edges.getIntervals()) {
					String from = interval.getFrom();
					String to = interval.getTo();
					if (replacements.containsKey(from)) {
						interval.setFrom(replacements.get(from));
					}
					if (replacements.containsKey(to)) {
						interval.setTo(replacements.get(to));
					}
				}
			}
		}

		// Write map to output file
		XmlModelWriter writer = new XmlModelWriter();

		OutputStream output = Files.newOutputStream(fileOutput);
		writer.write(output, model.getData(), model.getViews());
		output.close();
	}

	private static void rename(String name, String newName)
	{
		Station station = nameToStation.get(name);
		if (station == null) {
			logger.warn(String.format("Station not found: '%s'", name));
			return;
		}
		System.out.println(
				String.format("Replacing '%s' with '%s'", name, newName));
		station.setName(newName);
	}

}
