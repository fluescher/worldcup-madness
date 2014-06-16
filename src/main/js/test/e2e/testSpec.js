describe('index', function() {

  browser.get('index.html');

  it('should load the page', function() {
    browser.pause();
    expect(element.all(by.css('h1')).first().getText()).toBe('Test');
  });
});

