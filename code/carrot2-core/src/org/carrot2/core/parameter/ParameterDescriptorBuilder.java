package org.carrot2.core.parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.*;

import org.carrot2.core.constraint.*;
import org.carrot2.util.ClassUtils;

/**
 * 
 */
public class ParameterDescriptorBuilder
{
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public static Collection<ParameterDescriptor> getParameterDescriptors(Class<?> clazz,
        BindingPolicy policy)
    {
        // Output array of parameters.
        final ArrayList<ParameterDescriptor> params = new ArrayList<ParameterDescriptor>();

        // Get the field names that correspond to the requested policy.
        final Collection<Field> bindableFields = getFieldMap(clazz, policy).values();

        // This is a little hacky -- to get the default values we must create an instance
        // of the class. Assuming that we have no-parameter constructors
        // and an explicit lifecycle method doing the (usually costly) initialization,
        // we can safely create this instance here.
        Object instance;
        try
        {
            instance = clazz.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not create instance of class: "
                + clazz.getName());
        }

        for (final Field field : bindableFields)
        {
            final String fieldName = BindableUtils.getFieldKey(field);

            List<Constraint> constraints = new ArrayList<Constraint>();

            for (Annotation annotation : field.getAnnotations())
            {
                if (ConstraintFactory.isConstraintAnnotation(annotation.annotationType()))
                {
                    constraints.add(ConstraintFactory.createConstraint(annotation));
                }
            }
            Constraint constraint = null;
            if (constraints.size() == 1)
            {
                constraint = constraints.get(0);
            }
            else if (constraints.size() > 1)
            {
                constraint = new CompoundConstraint(constraints);
            }

            Object fieldValue;
            try
            {
                fieldValue = field.get(instance);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Could not retrieve default value of field: "
                    + fieldName);
            }

            params.add(new ParameterDescriptor(fieldName, ClassUtils.boxPrimitive(field
                .getType()), fieldValue, constraint));
        }

        return params;
    }

    /*
     * 
     */
    public static Map<String, Field> getFieldMap(Class<?> clazz, BindingPolicy policy)
    {
        final Collection<Field> fieldSet = BindableUtils
            .getFieldsFromBindableHierarchy(clazz);
        final Field [] fields = fieldSet.toArray(new Field [fieldSet.size()]);
        final Map<String, Field> result = new HashMap<String, Field>(fields.length);

        // TODO: Should we remember or somehow reset the accessibility for non-descriptor
        // fields upon returning from this method?
        AccessibleObject.setAccessible(fields, true);
        for (final Field field : fields)
        {
            final Parameter binding = field.getAnnotation(Parameter.class);
            if (binding == null)
            {
                continue;
            }

            if (binding.policy() == policy)
            {
                if (result.put(BindableUtils.getFieldKey(field), field) != null)
                {
                    throw new RuntimeException("A field with duplicated key exist: "
                        + BindableUtils.getFieldKey(field));
                }
            }
        }

        return result;
    }
}
