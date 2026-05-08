/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module 'markdown-it' {
  class MarkdownIt {
    constructor(options?: any)
    render(text: string): string
  }
  export default MarkdownIt
}
