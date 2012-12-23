SLF4J Plus
==========

Advanced SLF4J logger adding some nice features and Eclipse RCP
integration.

* Log transactions allow to group log messages emitted from a thread or its child threads between a start and end point
* Inside transactions create child transactions or arbitrary message groups
* Show log messages inside the Eclipse Error Log view - organized according to log transactions and groups
* Emit special log messages requesting the user's attention, w/o the need to introduce UI dependencies to your bundle
* Display these messages either through a dialog or a status item in an Eclipse RCP application

Please see the [Wiki](https://github.com/igd-geo/slf4j-plus/wiki) for
more details on features and how to use this library.

License
-------

This library has been released under the GNU Lesser General Public
License (LGPL) v3.0.

