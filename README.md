# Moodernize
A code modernizing transcompiler (C to Java)

In order to successfully compile the project, EMF, Xtext, CDT and JDT technologies must be installed. If that is done, OOGen implementation classes must be generated using EMF.
If the project successfully compiles, run the project (e.g. hu.bme.aut.moodernize.c2j) as an eclipse application. This installs the plugin in a runtime eclipse.
To run the tool, look for the "Moodernize C Project" option in the context menu of C projects. Note that all source and header files must be placed in a source folder otherwise the transformer will ignore them.
