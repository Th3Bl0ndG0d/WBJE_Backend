const puppeteer = require('puppeteer');
const path = require('path');

(async () => {
    const browser = await puppeteer.launch();
    const page = await browser.newPage();

    const filePath = 'file://' + path.resolve('./api-documentatie.html');

    await page.goto(filePath, { waitUntil: 'networkidle0' });

    await page.pdf({
        path: 'api-documentatie.pdf',
        format: 'A4',
        printBackground: true,
        margin: {
            top: '20mm',
            right: '15mm',
            bottom: '20mm',
            left: '15mm'
        }
    });

    await browser.close();
})();
