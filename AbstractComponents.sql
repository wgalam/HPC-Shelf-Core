SELECT 
  abstract_component.ac_name, 
  abstract_component.ac_id, 
  abstract_component.supertype_id, 
  context_parameter.cp_id, 
  context_parameter.cp_name, 
  context_parameter.kind_id, 
  context_parameter.bound_value, 
  context_parameter.bound_id, 
  context_parameter.parameter_type, 
  context_contract.cc_name, 
  context_contract.kind_id, 
  context_contract.owner_id
FROM 
  public.abstract_component, 
  public.context_parameter, 
  public.context_contract, 
  public."user"
WHERE 
  abstract_component.ac_id = context_parameter.ac_id AND
  context_parameter.bound_id = context_contract.cc_id AND
  context_contract.owner_id = "user".user_id;
