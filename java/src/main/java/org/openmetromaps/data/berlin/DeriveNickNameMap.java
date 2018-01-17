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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.openmetromaps.maps.MapModel;
import org.openmetromaps.maps.model.Station;
import org.openmetromaps.maps.xml.DesktopXmlModelReader;
import org.openmetromaps.maps.xml.XmlModel;
import org.openmetromaps.maps.xml.XmlModelConverter;
import org.openmetromaps.maps.xml.XmlModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.system.utils.SystemPaths;
import de.topobyte.xml.domabstraction.iface.ParsingException;

public class DeriveNickNameMap
{

	final static Logger logger = LoggerFactory
			.getLogger(DeriveNickNameMap.class);

	private static Map<String, Station> nameToStation = new HashMap<>();

	public static void main(String[] args) throws ParsingException, IOException,
			ParserConfigurationException, TransformerException
	{
		Path mainDir = SystemPaths.CWD.getParent();
		Path fileInput = mainDir.resolve("schematic.xml");
		Path fileOutput = mainDir.resolve("nicknames.xml");

		InputStream input = Files.newInputStream(fileInput);
		XmlModel xmlModel = DesktopXmlModelReader.read(input);
		input.close();

		XmlModelConverter converter = new XmlModelConverter();
		MapModel model = converter.convert(xmlModel);

		List<Station> stations = model.getData().stations;
		for (Station station : stations) {
			nameToStation.put(station.getName(), station);
		}

		rename("Alexanderplatz", "Alex");
		rename("Zoologischer Garten", "Zoo");
		rename("Kottbusser Tor", "Kotti");

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
		station.setName(newName);
	}

}
