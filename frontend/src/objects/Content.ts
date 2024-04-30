abstract class Content {
  blockType:
    | 'title'
    | 'text'
    | 'picture'
    | 'emotion'
    | 'weather'
    | 'kpt'
    | '4f';

  constructor(
    blockType:
      | 'title'
      | 'text'
      | 'picture'
      | 'emotion'
      | 'weather'
      | 'kpt'
      | '4f',
  ) {
    this.blockType = blockType;
  }
}

class TitleContent extends Content {
  title: string;

  constructor(title: string) {
    super('title');
    this.title = title;
  }
}

class TextContent extends Content {
  text: string;

  constructor(text: string) {
    super('text');
    this.text = text;
  }
}

class PictureContent extends Content {
  path: string;

  constructor(path: string) {
    super('picture');
    this.path = path;
  }
}

class EmotionContent extends Content {
  emoji: string;
  description: string;

  constructor(emoji: string, description: string) {
    super('emotion');
    this.emoji = emoji;
    this.description = description;
  }
}

class WeatherContent extends Content {
  weather: string;

  constructor(weather: string) {
    super('weather');
    this.weather = weather;
  }
}

class KPTContent extends Content {
  keepStr: string;
  problemStr: string;
  tryStr: string;

  constructor(keepStr: string, problemStr: string, tryStr: string) {
    super('kpt');
    this.keepStr = keepStr;
    this.problemStr = problemStr;
    this.tryStr = tryStr;
  }
}

class FourFContent extends Content {
  feeling: string;
  focus: string;
  fix: string;
  future: string;

  constructor(feeling: string, focus: string, fix: string, future: string) {
    super('4f');
    this.feeling = feeling;
    this.focus = focus;
    this.fix = fix;
    this.future = future;
  }
}

export {
  Content,
  TitleContent,
  TextContent,
  PictureContent,
  EmotionContent,
  WeatherContent,
  KPTContent,
  FourFContent,
};
