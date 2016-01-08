package com.jabue.gsl

import groovy.xml.MarkupBuilder
import com.jabue.src.domain.*

class SitemapController {

    def declaration = [version: "1.0", encoding: "UTF-8"]

    def xmlns = [
            xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9",
            'xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance",
            'xsi:schemaLocation': "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"
    ]

    def index() {

        Collection<SiteMapProperty> properties = initProperties()

        render(text: generateSiteMap(properties),contentType: "text/xml", encoding: "UTF-8")
    }

    private String generateSiteMap(Collection<SiteMapProperty> properties) {
        StringWriter writer = new StringWriter()
        MarkupBuilder mkp = new MarkupBuilder(writer)
        mkp.mkp.xmlDeclaration(declaration)
        mkp.urlset(xmlns) {
            properties.each {
                getUrlXml(mkp, it)
            }
        }
        return writer.toString()
    }

    private void getUrlXml(MarkupBuilder mkp, SiteMapProperty property) {

        mkp.url {
            if (!property.controllerName) {
                loc(g.createLink(absolute: true, uri: '/'))
            } else if (!property.actionName) {
                loc(g.createLink(absolute: true, controller: property.controllerName))
            } else {
                loc(g.createLink(absolute: true, controller: property.controllerName, action: property.actionName))
            }
            changefreq(property.freq)
            priority(property.prior)
        }
    }

    private Collection<SiteMapProperty> initProperties() {
        Collection<String> simpleControllers = [
                '', 'search', 'about-us', 'terms', 'contact-us',
                'q-and-a', 'seattle-comming-soon', 'toronto-comming-soon', 'become-a-host'
        ]
        Collection<SiteMapProperty> properties = []

        simpleControllers.each {
            SiteMapProperty temp = new SiteMapProperty()
            temp.controllerName = it
            properties.add(temp)
        }

        // search?assetCategory=户外游
//        SiteMapProperty searchCateg1 = new SiteMapProperty()
//        searchCateg1.controllerName = 'search'
//        searchCateg1.parameters = ["assetCategory": "户外游"]
//        properties.add(searchCateg1)
//        // search?assetCategory=美食游
//        SiteMapProperty searchCateg2 = new SiteMapProperty()
//        searchCateg2.controllerName = 'search'
//        searchCateg2.parameters = ["assetCategory": "美食游"]
//        properties.add(searchCateg2)
//
//        // article/List
//        SiteMapProperty articleList = new SiteMapProperty()
//        articleList.controllerName = 'article'
//        articleList.actionName = 'list'
//        properties.add(articleList)
//        // article/show/id
//        SiteMapProperty articleShow = new SiteMapProperty()
//        articleShow.controllerName = 'article'
//        articleShow.actionName = 'show'
//        articleShow.uId = Article.findAll().collect {it.id}
//        properties.add(articleShow)
//        // listing/show/id
//        SiteMapProperty listingShow = new SiteMapProperty()
//        listingShow.controllerName = 'listing'
//        listingShow.actionName = 'show'
//        listingShow.uId = Listing.findAll().collect {it.id}
//        properties.add(listingShow)

        return properties
    }

}
