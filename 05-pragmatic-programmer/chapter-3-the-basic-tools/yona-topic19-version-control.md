# Version Control

## Always Use Version Control
* Version Control System (VCS) is like a gian 'undo' key. But that's only the tip of an iceberg.
* Collaboration, deployment pipelines, issue tracking, general team interaction.
* Note: Shared Directories are NOT version control! If two instances simultaneously make changes, the overall state can become corrupted, and there's no telling how much damage will be done.

## It Starts at the Source
* VCS keep track of every changes made in your source code and documentation. You may always go back to a previous version of your software.
* VCS can answer (invaluable for bug-tracking, performance, and quality purposes): 
    * Who made changes in this line of code?
    * What's the difference between the current version and last week's?
    * How many lines of code did we change in this release?
    * Which files get changed most often?
* Can identify releases of your software.
* May keep the files they maintain in a central repository (great candidate for archiving).
* Allows users to work concurrently on the same set of files. The system manages the merging of the changes when the files are sent back to the repository.

## Branching Out
* Branches: VCS feature that allows isolate islands of development.
* Merging a branch to another branch is available.
* Can develop separate features without interfering with each other.
* Branches are at the heart of a team's project workflow. Very easy to find solutions for workflow issues.

## Version Control as a Project Hub
* Many VCS don't need any hosting - they are completely decentralized, with each developer cooperating on a peer-to-peer basis. 
* However, having a central repository (hosting your repository) can allow you to take advantage of a ton of integrations to make the project flow easier.
* If hosting with a third party, look for :
    * Good security and access control.
    * Intuitive UI.
    * Ability to do everything from the command line.
    * Automated builds and tests
    * Good support for branch merging (pull requests).
    * Issue management (ideally integrated into commits and merges, so you can keep metrics).
    * Good reporting. (Kanban board-like display of pending issues and tasks)
    * Good team communications. (emails/notifications on changes, wiki, etc)

* Many teams have their VCS configured so that a push to a particular branch will automatically build the system, run tests, and if successful deploy the new code into prod.

## Challenges
* Knowing you can roll back to any previous state using the VCS is one thing, but can you actually do it? Do you know the commands to do it properly? Learn them now, not when disaster strikes and you're under pressure.

* Spend some time thinking about recovering your own laptop environment in case of a disaster. What would you need to recover? Many of the things you need are just text files. If they're not in a VCS (hosted off your laptop), find a way to add them. Then think about the other stuff: installed applications, system configuration, and so on. How can you express all that stuff in text files so it, too, can be saved?