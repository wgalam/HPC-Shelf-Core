SELECT 
  abstract_component.ac_name, 
  abstract_component.ac_id, 
  abstract_component.supertype_id, 
  context_parameter.cp_id, 
  context_parameter.cp_name, 
  context_parameter.kind_id, 
  context_parameter.bound_value, 
  context_parameter.parameter_type
FROM 
  public.abstract_component, 
  public.context_parameter
WHERE 
  abstract_component.ac_id = context_parameter.ac_id AND
  context_parameter.bound_id IS NULL 
ORDER BY
  abstract_component.ac_id ASC;
