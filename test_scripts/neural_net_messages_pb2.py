# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: neural_net_messages.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)




DESCRIPTOR = _descriptor.FileDescriptor(
  name='neural_net_messages.proto',
  package='',
  serialized_pb='\n\x19neural_net_messages.proto\"%\n\nDataVector\x12\x0b\n\x03vec\x18\x01 \x03(\x01\x12\n\n\x02id\x18\x02 \x01(\t\":\n\x0eNeuralNetInput\x12\x0e\n\x06net_id\x18\x01 \x01(\t\x12\x18\n\x03mat\x18\x02 \x03(\x0b\x32\x0b.DataVector\"S\n\x0fNeuralNetOutput\x12\x0f\n\x07success\x18\x01 \x01(\x08\x12\x18\n\x03mat\x18\x02 \x03(\x0b\x32\x0b.DataVector\x12\x15\n\rerror_message\x18\x03 \x01(\tB5\n com.hello.suripu.api.datascienceB\x11NeuralNetMessages')




_DATAVECTOR = _descriptor.Descriptor(
  name='DataVector',
  full_name='DataVector',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='vec', full_name='DataVector.vec', index=0,
      number=1, type=1, cpp_type=5, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='id', full_name='DataVector.id', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=29,
  serialized_end=66,
)


_NEURALNETINPUT = _descriptor.Descriptor(
  name='NeuralNetInput',
  full_name='NeuralNetInput',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='net_id', full_name='NeuralNetInput.net_id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='mat', full_name='NeuralNetInput.mat', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=68,
  serialized_end=126,
)


_NEURALNETOUTPUT = _descriptor.Descriptor(
  name='NeuralNetOutput',
  full_name='NeuralNetOutput',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='success', full_name='NeuralNetOutput.success', index=0,
      number=1, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='mat', full_name='NeuralNetOutput.mat', index=1,
      number=2, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='error_message', full_name='NeuralNetOutput.error_message', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=128,
  serialized_end=211,
)

_NEURALNETINPUT.fields_by_name['mat'].message_type = _DATAVECTOR
_NEURALNETOUTPUT.fields_by_name['mat'].message_type = _DATAVECTOR
DESCRIPTOR.message_types_by_name['DataVector'] = _DATAVECTOR
DESCRIPTOR.message_types_by_name['NeuralNetInput'] = _NEURALNETINPUT
DESCRIPTOR.message_types_by_name['NeuralNetOutput'] = _NEURALNETOUTPUT

class DataVector(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _DATAVECTOR

  # @@protoc_insertion_point(class_scope:DataVector)

class NeuralNetInput(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _NEURALNETINPUT

  # @@protoc_insertion_point(class_scope:NeuralNetInput)

class NeuralNetOutput(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _NEURALNETOUTPUT

  # @@protoc_insertion_point(class_scope:NeuralNetOutput)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), '\n com.hello.suripu.api.datascienceB\021NeuralNetMessages')
# @@protoc_insertion_point(module_scope)
