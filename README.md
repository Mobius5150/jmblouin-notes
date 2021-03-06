jmblouin-notes
==============
This is a simple todo list application for the CMPUT 301 class.

This application uses the following third-party resources:
- [Android Action Bar Icon Pack](https://developer.android.com/design/style/iconography.html) ([Creative Commons Attribution 2.5](http://creativecommons.org/licenses/by/2.5/))

Additionally, much of the API use, and general design of the application was inspired from the [Android Developers](http://developer.android.com) site. These articles in particular were referenced alot:

- [ListActivity Class](http://developer.android.com/reference/android/app/ListActivity.html)
- [Adapter Class](http://developer.android.com/reference/android/widget/Adapter.html)
- [Interacting with other Applications](http://developer.android.com/training/basics/intents/index.html)
- [Adding the Action Bar](http://developer.android.com/training/basics/actionbar/index.html)
- [Building a dynamic ui with fragments](http://developer.android.com/training/basics/fragments/index.html)
- [Saving Files](http://developer.android.com/training/basics/data-storage/files.html)

Note that no code blocks were directly taken from the Android Developer articles. Rather, the information in the articles was used to learn the use of the Android API, and common practices in structuring an application.

Lastly, the assignment was discussed with the following students (by CCID), however no code or specific details were exchanged:
- sajust
- lamorie

## Usage

### Adding Items
To add an item, go to a todo group view (Archive or Todo Items), click the plus button at the bottom, and enter a name for the item. Click ok.

### Checking/UnChecking items
To check or uncheck an item tap on the checkbox next to the item name, or on the item text itself.

### Selecting Items
To select a todo item, tap on the whitespace around the item text. Alternatively you can long-click the item for the same affect. Once a single item is selected, you may select multiple items.

### Deleting Items
In a todo group view, select an item and click the delete icon on the menu bar.

### Moving/Archiving Items
In a todo group view, select one or more items and click the move icon on the menu bar. Select the todo group you would like to move the item to.

### Emailing Items

#### Email All Todo Items
When at the main view in the application, click the email button on the menu bar and select an application to use to send the email.

#### Email All Todo Items in a Group
While viewing a todo group, click the email button on the menu bar and select an application to use to send the email.  

#### Email One or More Todo Items
While viewing a todo group, select an item by clicking on the whitespace around its name, this will begin a selection. Select any other items you would like to send and click the email button on the menu bar.

## UML Diagrams
If you would like to see UML diagrams of the applications' structure, please see the `jmblouin-notes-uml.png` file in the `doc` directory. Note that all of the `jmblouin-notes-uml.*` files are the same. For best results open the .png file.

## License
This application is copyrighted to `Michael Blouin` in `2014`, and released under the APACHE 2.0 License. See `LICENSE.txt` for more information.
