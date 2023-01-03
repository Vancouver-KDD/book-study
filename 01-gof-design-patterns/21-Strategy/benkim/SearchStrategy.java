package Strategy;

interface SearchStrategy {
    public void search ();
}

class SearchStrategyAll implements SearchStrategy {
    public void search () {
        System.out.println("SEARCH ALL");
        // 전체검색하는 코드
    }
}

class SearchStrategyImage implements SearchStrategy {
    public void search () {
        System.out.println("SEARCH IMAGE");
        // 이미지검색하는 코드
    }
}

class SearchStrategyNews implements SearchStrategy {
    public void search () {
        System.out.println("SEARCH NEWS");
        // 뉴스검색하는 코드
    }
}

class SearchStrategyMap implements SearchStrategy {
    public void search () {
        System.out.println("SEARCH MAP");
        // 지도검색하는 코드
    }
}