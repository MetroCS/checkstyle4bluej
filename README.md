# checkstyle4bluej
## Checkstyle Extension for BlueJ

This updates the *BlueJ Checkstyle Extension* (http://bluejcheckstyle.sourceforge.net) to work with BlueJ **4.1**, Checkstyle **8.4**, and Java **1.8**.

The original author of the extension indicates a desire to rehost and maintain the project. (Update 20 March 2018: See https://sourceforge.net/p/bluejcheckstyle/code/ref/master/)

The intent of this repository is to tide over the user community until those activities can take place.  Thus, minimal modifications to original sources, build process, and documentation have been made, as necessary to support the current environment.

Primary contributors to *this updating effort* were students enrolled in the Fall 2017 semester of [CS4250](http://www.jodypaul.com/cs/sweprin/), *Software Engineering Principles,* at [Metropolitan State University of Denver](http://www.msudenver.edu).

---
### Abbreviated Usage Instructions
#### Overview

The *BlueJ Checkstyle Extension* is a wrapper for [BlueJ](http://bluej.org) to use [Checkstyle](http://checkstyle.sourceforge.net/), a source code analysis tool to help programmers write Java code that adheres to a coding standard.

The user can choose the Checkstyle configuration file that specifies which checks are applied to the Java source code. The extension distribution includes sample configuration files for the Sun Code Conventions, course-specific configuration files used at Virginia Tech and Metro State Denver, and the Big Java Coding Guidelines.

#### Requirements

[BlueJ](http://bluej.org) 4.1.x running on [JDK](http://www.oracle.com/technetwork/java/javase/overview/index.html) 8 (1.8.x) or higher.

#### Download

The update of the BlueJ Checkstyle extension can be downloaded from [the GitHub repository](https://github.com/MetroCS/checkstyle4bluej) using this direct link to the [extension jar file](https://github.com/MetroCS/checkstyle4bluej/raw/master/checkstyle-extension-5.4.1.jar).

#### Installation

* Download the latest version of the [extension jar file](https://github.com/MetroCS/checkstyle4bluej/raw/master/checkstyle-extension-5.4.1.jar).

* Place the jar file in BlueJ's _extensions_ directory:

  * Under Windows or Unix, place the jar in `<BLUEJ_HOME>/lib/extensions`, where `<BLUEJ_HOME>` is the directory where BlueJ is installed.
  * Under Mac OSX, place the jar in `/Applications/BlueJ.app/Contents/Resources/Java/extensions` (control-click `BlueJ.app` and choose *Show Package Contents*) or in `~/Library/Preferences/org.bluej/extensions`.
  * If you are upgrading from an earlier version, remove the older `checkstyle-extension*.jar` file from your extensions directory.

#### Usage

* Open BlueJ.
* To run a check on all files in the current package, choose `Checkstyle` from the `Tools` menu.

#### Setting User Preferences

* The extension uses its built-in [`default_checks.xml`](https://github.com/MetroCS/checkstyle4bluej/blob/master/docs/default_checks.xml) file unless you specify a different configuration file in Preferences.

* To specify the Checkstyle configuration file or properties file:
  * Choose `Preferences...` from the `Tools` menu (or from the `BlueJ` menu under Mac OSX).
  * In the Preferences panel, click on the `Extensions` tab.
  * In the "Checkstyle" section, enter the file name for the checkstyle configuration XML file and/or properties file you wish to use.
  * In addition to local file names, you can also use URLs in the preferences settings.
  * To use one of the built-in configuration files, simply enter one of these file names with no path or directory:
    * [`sun_checks.xml`](https://github.com/MetroCS/checkstyle4bluej/blob/master/docs/sun_checks.xml) ([Sun Java code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html))
    * [`bigjava_checks.xml`](http://bluejcheckstyle.cvs.sourceforge.net/bluejcheckstyle/bluejcheckstyle/docs/bigjava_checks.xml?view=markup) ([*Big Java* coding conventions](http://horstmann.com/bigj/style.html))
    * [`cs1114_checks.xml`](http://bluejcheckstyle.cvs.sourceforge.net/bluejcheckstyle/bluejcheckstyle/docs/bigjava_checks.xml?view=markup) (Virginia Tech CS style)
    * [`cs4250_checks.xml`](https://github.com/MetroCS/checkstyle4bluej/blob/master/docs/cs4250_checks.xml) (Metro State Denver CS style)
