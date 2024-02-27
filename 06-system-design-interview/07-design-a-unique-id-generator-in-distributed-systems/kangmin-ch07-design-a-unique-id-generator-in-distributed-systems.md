# Chapter 7 : DESIGN A UNIQUE ID GENERATOR IN DISTRIBUTED SYSTEMS


## Step 1 - Understand the problem and establish design scope

- 이 단계에서 목표는 설계에 대한 명확한 이해를 하는 것과
- 구체적인 요구 사항 정의를 하라는 것이다.
- 예를 들면, ID 는 unique 한 지, number 는 정확히 어떤 number 인지, date sort 는 어떻게 할 건지;; 뭐 그런것들 자세히 다 물어봐라 그런 얘기다.

## Step 2 - Propose high-level design and get buy-in

- Step 1 보다 조금 더 고수준으로 설계를 한다.
- 옵션을 몇개 주고 장단점을 비교해 가면서 방향을 같이 합의 하도록 하자
- 예를 들면, 뭐... 멀티 마스터 복제를 어떻게 할 것인지.
- potential issue 들에 대한 언급 등등
- 설계를 하나만 제시하지 말고 여러 개 떠오르는 대로 얘기를 해보고, 어떤 방식을 선호를 하는지 같의 정하는게 좋다


## Step 3 - Design deep dive

- 위에서 동의된 설계를 가지고 심층 분석을 한다
- 위에서 정의하지 않았던 주요 구성 요소들을 정의를 하고 작동 방식에 대해 설명을 하면서 어필을 한다.
- 각 요소들에 대해서 설명을 할 것들이 있으면 여기서 디테일 하게 해도 되고
- 그리고 시스템 확장 성과 에러 핸들링도 여기서 언급하자

## Step 4 - Wrap up

- 마지막으로는 전체적으로 정리를 하면서
- 개인적으로는 다시한번 내 설계에 있어서 어필하고자 하는 부분을 강조하면서 정리를 할 수도 있고
- 혹시나 빼먹은게 있다면 여기서 언급해도 괜찮을 것 같다.
