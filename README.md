This is the data project for the
[OpenMetroMaps](http://github.com/OpenMetroMaps)
for Berlin, Germany.

# Copyright

All data from OpenStreetMap within this projects
and its derivative data sets are licensed under
the terms of the
[Open Data Commons Open Database License (ODbL)](https://opendatacommons.org/licenses/odbl/).

# Exploring the data

Here are links to the demo page to view the maps from this repository online:
* [Geographic Map](https://demo.openmetromaps.org/berlin/geographic.html)
* [Schematic Map](https://demo.openmetromaps.org/berlin/schematic.html)
* [Nicknames Map](https://demo.openmetromaps.org/berlin/nicknames.html)

# Working with the tools

To set up the tools within this project, change to the `java` directory
and build the executables:

    cd java
    gradle clean create
    cd ..

Afterwards you can list the stations like this:

    ./java/scripts/list-stations.sh

To derive the nick-names map from the schematic map, run this:

    ./java/scripts/derive-nick-name-map.sh

To view any of the maps run this (you need to have the
[main OpenMetroMaps
executable](https://github.com/OpenMetroMaps/OpenMetroMaps/blob/master/java/README.md)
on your `PATH`):

    openmetromaps-cli map-viewer --input schematic.xml
    openmetromaps-cli map-viewer --input geographic.xml
    openmetromaps-cli map-viewer --input nicknames.xml

To start editing a map, run this:

    openmetromaps-cli map-editor --input schematic.xml
