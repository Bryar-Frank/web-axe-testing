Feature: Testing Deque Practice Site

  Scenario: Testing Accessibility Standards
    Given a certain webpage "https://dequeuniversity.com/demo/mars/"
    When "<type>" accessibility checks are performed
    Then there are no violations

    Examples:
      | type           |
      | Full           |
      | WCAG Only      |
      | Best Practices |
