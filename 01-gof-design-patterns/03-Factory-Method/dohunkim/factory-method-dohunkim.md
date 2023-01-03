`Product`<sub>Page</sub>  and `ConcreteProduct`<sub>All other classes</sub>
```c#
    abstract class Page { }
    class SkillsPage : Page { }
    class EducationPage : Page { }
    class ExperiencePage : Page { }
    class IntroductionPage : Page { }
    class ResultsPage : Page { }
    class ConclusionPage : Page { }
    class SummaryPage : Page { }
    class BibliographyPage : Page { }
```

`Factory`<sub>Creator(Document)</sub>
```c#
    abstract class Document
    {
        // Constructor calls abstract Factory method
        protected Document()
        {
            CreatePages();
        }
        public readonly List<Page> Pages = new();

        // Factory Method
        protected abstract void CreatePages();
    }
    class Resume : Document
    {
        // Factory Method implementation
        protected override void CreatePages()
        {
            Pages.Add(new SkillsPage());
            Pages.Add(new EducationPage());
            Pages.Add(new ExperiencePage());
        }
    }
    class Report : Document
    {
        // Factory Method implementation
        protected override void CreatePages()
        {
            Pages.Add(new IntroductionPage());
            Pages.Add(new ResultsPage());
            Pages.Add(new ConclusionPage());
            Pages.Add(new SummaryPage());
            Pages.Add(new BibliographyPage());
        }
    }
```

`Program`
```c#
    class Program
    {
        private static void Main()
        {
            var documents = new Document[2];
            documents[0] = new Resume();
            documents[1] = new Report();
            foreach (var document in documents)
            {
                Console.WriteLine("\n" + document.GetType().Name + "--");
                foreach (var page in document.Pages)
                {
                    Console.WriteLine(" " + page.GetType().Name);
                }
            }
            Console.ReadKey();
        }
    }
```

Output
```
Resume--
 SkillsPage
 EducationPage
 ExperiencePage

Report--
 IntroductionPage
 ResultsPage
 ConclusionPage
 SummaryPage
 BibliographyPage
```
https://www.dofactory.com/net/factory-method-design-pattern<sup>a)</sup>


&nbsp;
## References
#### a) https://www.dofactory.com/net/factory-method-design-pattern