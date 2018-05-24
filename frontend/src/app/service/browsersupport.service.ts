import {Injectable} from '@angular/core';
import {WindowRef} from "../windowref";
import {environment} from "../../environments/environment.prod";

/*
* this code is heavily based on the following stack overflow answer
* https://stackoverflow.com/questions/46116284/angular-2-how-to-show-warning-page-if-browser-is-unsupported
*
*/
@Injectable()
export class BrowsersupportService {

  private browser: string;
  readonly version: number;
  private versionSearchString: any;
  readonly dataBrowser = [];

  constructor(private window: WindowRef) {
    const navigator = this.window.nativeWindow.navigator;
    this.dataBrowser = [
      {string: navigator.userAgent, subString: "Edge", identity: "MS Edge"},
      {string: navigator.userAgent, subString: "MSIE", identity: "Explorer"},
      {string: navigator.userAgent, subString: "Trident", identity: "Explorer"},
      {string: navigator.userAgent, subString: "Firefox", identity: "Firefox"},
      {string: navigator.userAgent, subString: "Opera", identity: "Opera"},
      {string: navigator.userAgent, subString: "OPR", identity: "Opera"},
      {string: navigator.userAgent, subString: "Chrome", identity: "Chrome"},
      {string: navigator.userAgent, subString: "Safari", identity: "Safari"}
    ];

    this.browser = this.searchString(this.dataBrowser)
    this.version = this.searchVersion(navigator.userAgent)
  }

  private searchString(data) {
    for (let i = 0; i < data.length; i++) {
      const dataString = data[i].string;
      this.versionSearchString = data[i].subString;

      if (dataString.indexOf(data[i].subString) !== -1) {
        return data[i].identity;
      }
    }
  }

  private searchVersion(dataString) {
    const index = dataString.indexOf(this.versionSearchString);
    if (index === -1) {
      return;
    }

    const rv = dataString.indexOf("rv:");
    if (this.versionSearchString === "Trident" && rv !== -1) {
      return parseFloat(dataString.substring(rv + 3));
    } else {
      return parseFloat(dataString.substring(index + this.versionSearchString.length + 1));
    }
  }

  isBrowserSupported(): boolean {
    for (let browser of environment.supportedBrowsers) {
      const browserName = browser.name;
      const browserVersion = browser.version;
      if (browserName.toLowerCase() === this.browser.toLowerCase() && this.version >= browserVersion) {
        return true;
      }
    }
    return false;
  }
}
