/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author sittiwatlcp
 */
public class EmptyFormFilter implements Filter {

    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Map m = request.getParameterMap();
        Set s = m.entrySet();
        Iterator it = s.iterator();

        while (it.hasNext()) {

            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

            
            //Try to do first if not affect i will remove this check
            if (entry == null || entry.getValue() == null || entry.getValue()[0] == null) {
                throw new NullPointerException("Parameter null");
            }

            String key = entry.getKey();
            String[] value = entry.getValue();

            if (value.length > 1) {
                for (String v : value) {
                    if (v.isEmpty()) {
                        throw new NoSuchElementException("Empty " + key);
                    }
                }
            } else {
                if (value[0].isEmpty()) {
                    throw new NoSuchElementException("Empty " + key);
                }
            }

        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
