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

import java.nio.file.Path;
import java.nio.file.Paths;

import de.topobyte.system.utils.SystemPaths;

public class Util
{

	public static Path repoDir()
	{
		String repoPath = System.getProperty("repo");
		Path repoDir = repoPath != null ? Paths.get(repoPath)
				: SystemPaths.CWD.getParent();

		return repoDir;
	}

}
