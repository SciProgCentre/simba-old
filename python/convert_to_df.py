import argparse
import json
import os
import io
from pathlib import Path
from zipfile import ZipFile

import dataforge
from dataforge import Meta
from dataforge.context import GLOBAL
from dataforge.io import Envelope, JsonMetaFormat, IOPlugin, TaglessEnvelopeFormat, BytesBinary, TaggedEnvelopeFormat
from dataforge.io.binary import EMPTY_BINARY


def load_meta(path):
    with open(path) as fin:
        return JsonMetaFormat().read_meta(fin)

def walk(root_path):
    files = os.listdir(root_path)
    if "meta.json" not in files:
        return None
    root_meta = load_meta(os.path.join(root_path, "meta.json"))

    dir_items = []
    file_items = {}
    for file in files:
        path = os.path.join(root_path, file)
        if os.path.isdir(path):
            temp = walk(path)
            if temp is not None:
                dir_items.append((file, path, temp))
        elif file != "meta.json":
            try:
                name, file_type, ext = file.rsplit(".", maxsplit=2)
            except Exception:
                continue
            if file_type  == "meta":
                file_items[name] = {}
                file_items[name]["meta"] = path

    for file in files:
        path = os.path.join(root_path, file)
        if file != "meta.json" and not os.path.isdir(path):
            name, ext = file.rsplit(".", maxsplit=1)
            if name in file_items.keys():
                file_items[name]["data"] = path


    return root_meta, file_items, dir_items


# def add_data_to_zip(zip, meta_path, data_path):
#     with open(meta_path) as fmeta:
#         meta = json.load(meta_path)
#     with open(data_path, "rb") as fdata:
#         data = fdata.read()
#     envelope = Envelope(Meta(meta), data)


def create_parser():
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('target', metavar='DIR', type=str, nargs='+',
                        help='an files for processing')
    parser.add_argument("--output", "-o", action="store", help="Output file name", default="output.zip.df")
    return parser

def add_envelope_to_zip(zip: ZipFile, path, envelope):
    # temp = io.StringIO()
    temp = io.BytesIO()
    io_plugin : IOPlugin =  GLOBAL.io()
    # io_plugin.write_envelope(temp, envelope, envelope_format=TaglessEnvelopeFormat)
    io_plugin.write_envelope(temp, envelope, envelope_format=TaggedEnvelopeFormat)
    zip.writestr(path, temp.getvalue())


def add_to_zip(zip, root_path, root_meta, file_items, dir_items):
    structure = []
    for key in file_items.keys():
        structure.append(
            {
                "name" : key,
                "type" : "FILE"
            }
        )
    for name, _, _ in dir_items:
        structure.append(
            {
                "name" : name,
                "type" : "DIRECTORY"
            }
        )
    root_meta["structure"] = structure
    add_envelope_to_zip(zip, os.path.join(root_path, "manifest.df"), Envelope(root_meta))

    for key, value in file_items.items():
        path = os.path.join(root_path, key + ".df")
        meta = load_meta(value["meta"])
        try:
            with open(value["data"], "rb") as fin:
                data = BytesBinary(fin.read())
        except Exception:
            data = EMPTY_BINARY
        add_envelope_to_zip(zip, path, Envelope(meta, data))

    for file, path, temp in dir_items:
        path = os.path.join(root_path, file)
        add_to_zip(zip, path, *temp)






def main():
    args = create_parser().parse_args()

    zip_path = args.output
    with ZipFile(zip_path, mode="w") as zip:
        for dir_ in args.target:
            root_meta, file_items, dir_items = walk(dir_)
            add_to_zip(zip, "", root_meta, file_items, dir_items)

if __name__ == '__main__':
    main()