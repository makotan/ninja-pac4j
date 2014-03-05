/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ninja.auth.pac4j;

import ninja.Context;
import ninja.Result;
import ninja.Results;
import org.pac4j.core.context.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.Map;

public class NinjaWebContext implements WebContext {
    private static final Logger logger = LoggerFactory.getLogger(NinjaWebContext.class);

    Context context;
    Result result = Results.ok();

    public NinjaWebContext(Context context) {
        this.context = context;
    }

    @Override
    public String getRequestParameter(String name) {
        return context.getParameter(name);
    }

    @Override
    public Map<String, String[]> getRequestParameters() {
        return context.getParameters();
    }

    @Override
    public String getRequestHeader(String name) {
        return context.getHeader(name);
    }

    @Override
    public void setSessionAttribute(String name, Object value) {
        logger.trace("setSessionAttribute name: {} , value: {}" , name , value);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeObject(value);
        } catch (IOException e) {
            throw new RuntimeException("" , e);
        }
        String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
        context.getSession().put(name , data);
    }

    @Override
    public Object getSessionAttribute(String name) {
        String data = context.getSession().get(name);
        if (data == null) {
            return null;
        }
        byte[] bytes = DatatypeConverter.parseBase64Binary(data);
        logger.trace("getSession {} {}", name, data);
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return in.readObject();
        } catch (Exception e) {
            throw new RuntimeException("unexpected exception", e);
        }
    }

    @Override
    public String getRequestMethod() {
        return context.getMethod();
    }

    @Override
    public void writeResponseContent(String content) {
        result.renderRaw(content);
    }

    @Override
    public void setResponseStatus(int code) {
        result.status(code);
    }

    @Override
    public void setResponseHeader(String name, String value) {
        result.addHeader(name , value);
    }

    public Result getResult() {
        return result;
    }
}
