```Python
import abc


class ObserverInterface(abc.ABC):
    """
    The interface that all observers must implement to receive notifications
    """

    @abc.abstractmethod
    def notify(self, message):
        """
        Responsible for reacting to the message that is passed in
        :param message: a string that the Core sends to observers
        """
        pass

class IPhoneUser(ObserverInterface):

    def __init__(self, name):
        """
        Initialize the Observer with a name
        :param name: a string
        """
        self.name = name

    def notify(self, message):
        """
        Responsible for reacting to the message that is passed in
        :param message: a string that the Core sends to observers
        """
        print(f"iPhone user {self.name} got the news: {message}")

    def __str__(self):
        return "iPhone user {}".format(self.name)

class AndroidUser(ObserverInterface):

    def __init__(self, name):
        """
        Initialize the Observer with a name
        :param name: a string
        """
        self.name = name

    def notify(self, message):
        """
        Responsible for reacting to the message that is passed in
        :param message: a string that the Core sends to observers
        """
        print(f"Android user {self.name} got the news: {message}")

    def __str__(self):
        return "Android user {}".format(self.name)

class Newspaper:
    """
    The Core or Subject in the observer design pattern. This class allows observers
    to be attached to it. Observers are then notified when new news is provided to the newspaper
    """

    def __init__(self):
        """
        Initialize the newspaper with an empty list of observers
        """
        self.observers = []
        pass


    def attach(self, *observers_list):
        """
        adds observer to observer list
        :param observer: ObserverInterface object
        """
        for observer in observers_list:
            print(f'attaching {observer}')
            self.observers.append(observer)

    def deattach(self, observer):
        """
        removes observer from observer list
        :param observer: ObserverInterface object
        """
        print(f'detaching {observer}')
        self.observers.remove(observer)

    def new_news(self, message):
        """
        notify doesn't care who's in the observer list, android user or iphone, it blindly
        iterates through all observers and notifies them of the message
        :param message: string indicating the news
        """
        for o in self.observers:
            o.notify(message)

# create Core/Subject newspaper
vancouver_sun = Newspaper()

# create two observers of different types
jeff = IPhoneUser('Jeff')
eric = AndroidUser('Eric')

# attach all observers to the newspaper
vancouver_sun.attach(jeff, eric)

#everything is setup to notify observers of news

# indicate to the newspaper that news has occurred. Newspaper will broadcast this to two observers
vancouver_sun.new_news("It's snowing today!")


# detach one observer
vancouver_sun.deattach(eric)

# indicate to the newspaper that news has occurred. Newspaper will broadcast this to one observer
vancouver_sun.new_news("Stocks are going up!")

```
