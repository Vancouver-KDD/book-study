# The Power of Plain Text
### plain text
- As Pragmatic Progammers, out base material isn't wood or iron, it's knowledge.
- Gather requirement as knowledge, and then express the knowledge in our designs, implementations, tests, and documents.
- The best format for storing knowledge persistently is plain text
- Virtually every tools support plain text to manipluate knowledge, both manually and programmatically.

### bianry foramt
- Data is separate from the data itself.
- Hard to figure out the meaning of data, need to understand the context.
- Absolutely meaningless without the application logic to parse it.
- Plain text can achieve a self-describing data stream


## What Is Plain Text?
- Plain text is made up of printable characters in a form that conveys information
```
*milk
*lettuce
*coffee
```

- not useufl plain text
```
h;sldfjas;;sdflsadkfasfkasd;sdfsdlf;28232;
```
```
Field19=467abe
```
- Reader has no idea what the significance of 467abe may be. 
> Tip25: Keep Knowledge in Plain text

## The power of text
- Plain text doesn't mean that the text is unstructured.
  - HTML, JSON, YAML, ...
  - HTTP, SMTP, IMAP, ...(Majority of the fundamental protocols on the net)
  
### Insurance Against Obsolescence
- Human-readable forms of data, and self-describing data, will outlive all other forms
- As long as the data survivies, you will have a chance to be able to use it.
- It is possible to parse with only partial knowledge of its format
  - To parse binary file, all the details of the entire format is needed.
#### Exmple of plain text : Social Security Numbers
```
<FIELD10>123-45-6789</FIELD10>
...
<FIELD10>567-89-0123</FIELD10>
...
<FIELD10>901-23-4567</FIELD10>
```
- It is possible to parse, find and extract data even there is no further information about this file

#### Example of binary file
```
AC232342414B11P
...
XY23423232323QTY
...
6T21010123456sAM
```
- It's hard to recognized the significance of the numbers.

#### Another of exmaple plain-text
```
<SOCIAL-SECURITY-NO>123-35-1234</SOCIAL-SECURITY-NO>
```
- "FIELD10" in first example does not help much ethier. This example amkes the exercise a no-brainer-and ensure that the data will outlive any project that created it.

### Leverage existing tools
Virtually every tool in the computing universe, from version control systems to editors to command-line tools, can operate on plain text.

#### The Unix Philosophy
```
Unix is famous for being designed around the philosophy of small, sharp tools, each
intended to do one thing well. This philosophy is enabled by using a common
underlying format—the line-oriented, plain-text file. Databases used for system
administration (users and passwords, networking configuration, and so on) are all kept
as plain-text files. (Some systems also maintain a binary form of certain databases as a
performance optimization. The plain-text version is kept as an interface to the binary
version.)
When a system crashes, you may be faced with only a minimal environment to restore
it (you may not be able to access graphics drivers, for instance). Situations such as this
can really make you appreciate the simplicity of plain text.
Plain text is also easier to search. If you can’t remember which configuration file
manages your system backups, a quick grep -r backup /etc should tell you.
```
- Easy to search / Restore system from crashes / Operation System... in every aspect, plain text works well
- Easy to be used in the Version Control system.
- File comparision tools, like diff and fc allows to see at a glance what changes have been made
- sum allows you to generate a checksum to monitor the file for accidental modification.

### Easier Testing
- Easy to add, update, or modify the test data without having to create any special to do so. 
- Plan-text output can be analyzed with shell commands or a simple scripts.

## Lowest Common Denominator
- Plain-text can be used even in the future at various system (Blockchain-base intelligent agent, in heterogeneous environments, ...)
- Plain-text is standard which can be used to communicate all parties.

## Challenges
- Design a small address book database(name, phone number, and so on) using a straightforward binary representation .
- Translate that format into a plain-text format using XML or JSON.
- For each version, add a new, variable-length field called directions in which you might enter directions to each person's house
- What issues come up regarding versioning and extensibility?
- Which form was easier to modify?
- What about converting existing data? 
