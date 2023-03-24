# Magic Item Creator

A program that allows users to create magic items with pixel art for tabletop rpgs and worldbuilding.

## Features

- List of user-created Magic Items that will be stored on the local machine
  - Items support a Name, Description, Type, Pixel Art, and Traits
- Full support for pixel art editing for created items with customizable resolution
- User-created magic traits can be added to a global list and can then be applied to items
- Search and Sort
  - Search by Name or Type currently supported
  - Sort by Name currently supported

## Building from Source

Intellij IDEA
1. Open the CSCD488_Group6 project
2. Select "Run" then select "Edit Configurations"
3. Select "+" to add a new configuration
4. Select "Maven"
5. Under "Run" put "compile javafx:run"
6. Click "OK"
7. Click "►"

Eclipse
1. Right click project
2. Run As -> Maven Build
3. In "Goals", add "clean javafx:run"
4. Click "Apply" -> "Run"
5. The Maven bulid should now be under Run Configurations

Command Line
1. Install [Maven](https://maven.apache.org/install.html)
2. Navigate to CSCD488_Group6/
3. Execute "mvn compile"
4. Execute "mvn clean javafx:run"

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

## License

This code is under the [Apache License, Version 2.0, January 2004](https://www.apache.org/licenses/LICENSE-2.0).
